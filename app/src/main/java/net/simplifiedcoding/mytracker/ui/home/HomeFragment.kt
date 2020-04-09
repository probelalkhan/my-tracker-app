package net.simplifiedcoding.mytracker.ui.home

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import net.simplifiedcoding.mytracker.MyTrackerApplication
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.data.db.MyTrackerDatabase
import net.simplifiedcoding.mytracker.data.db.Survey
import net.simplifiedcoding.mytracker.databinding.FragmentHomeBinding
import net.simplifiedcoding.mytracker.service.MyTrackerService
import net.simplifiedcoding.mytracker.ui.BaseFragment
import net.simplifiedcoding.mytracker.ui.utils.showDialog
import net.simplifiedcoding.mytracker.ui.utils.showInputDialog
import net.simplifiedcoding.mytracker.ui.utils.snackbar
import org.kodein.di.generic.instance
import java.util.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), SurveyCallback,
    RecyclerViewClickListener<Survey> {

    private val db: MyTrackerDatabase by instance()
    private val adapter = SurveyAdapter()

    private val appPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    else
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)


    companion object {
        const val PERMISSION_REQUEST_CODE = 119
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(false)
        requireActivity().actionBar?.setHomeButtonEnabled(false)

        adapter.listener = this
        binding.recyclerView.adapter = adapter


        binding.buttonAddSurvey.setOnClickListener {
            if (isTrackingServiceRunning(MyTrackerService::class.java)) MyTrackerService.stop(
                requireContext()
            )
            else requireContext().showInputDialog(R.layout.alert_dialog_survey, this@HomeFragment)
            setSurveyButtonText()
        }

        viewModel.getSurveys().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.setSurveys(it)
        })

        checkAndRequestPermission()
    }

    private fun setSurveyButtonText() {
        binding.buttonAddSurvey.text =
            if (isTrackingServiceRunning(MyTrackerService::class.java)) getString(R.string.stop_survey)
            else getString(R.string.start_survey)
    }

    override fun onResume() {
        super.onResume()
        setSurveyButtonText()
    }


    override fun getFragmentView() = R.layout.fragment_home

    override fun getViewModel() = HomeViewModel::class.java

    override fun onSurveyStarted(surveyName: String) {
        if (surveyName.isEmpty()) {
            requireView().snackbar(getString(R.string.survey_name_required))
            return
        }
        val survey = Survey(
            surveyName,
            null
        )
        launch {
            val id = db.getSurveyDao().insert(survey)
            MyTrackerService.start(MyTrackerApplication.appContext, id)
            requireActivity().finish()
        }
    }

    override fun onRecyclerViewClick(view: View, item: Survey) {
        val bundle = bundleOf(SurveyFragment.KEY_SURVEY_ID to item.id)
        view.findNavController().navigate(R.id.surveyFragment, bundle)
    }

    private fun checkAndRequestPermission(): Boolean {
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        for (perm in appPermissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionsNeeded.add(perm)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), listPermissionsNeeded.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val permissionResults =
                HashMap<String, Int>()
            var deniedCount = 0
            for (i in grantResults.indices) { // Add only permissions which are denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults[permissions[i]] = grantResults[i]
                    deniedCount++
                }
            }
            if (deniedCount != 0) {
                for ((permName, permResult) in permissionResults) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            permName
                        )
                    ) {
                        requireContext().showDialog(
                            "",
                            getString(R.string.location_permission_required),
                            getString(R.string.yes_grant_permission),
                            DialogInterface.OnClickListener { dialogInterface, _ ->
                                dialogInterface.dismiss()
                                checkAndRequestPermission()
                            },
                            getString(R.string.no_exit_app),
                            DialogInterface.OnClickListener { dialogInterface, _ ->
                                dialogInterface.dismiss()
                                requireActivity().finish()
                            },
                            false
                        )
                    } else {
                        requireContext().showDialog(
                            "",
                            getString(R.string.permission_denied),
                            getString(R.string.go_to_settings),
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.dismiss()
                                // Go to app settings
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", requireActivity().packageName, null)
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                requireActivity().finish()
                            },
                            getString(R.string.no_exit_app),
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.dismiss()
                                requireActivity().finish()
                            },
                            false
                        )
                        break
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun isTrackingServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
