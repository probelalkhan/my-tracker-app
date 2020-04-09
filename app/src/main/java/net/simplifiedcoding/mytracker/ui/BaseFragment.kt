package net.simplifiedcoding.mytracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import net.simplifiedcoding.mytracker.MyTrackerApplication
import net.simplifiedcoding.mytracker.data.prefs.PreferenceProvider
import net.simplifiedcoding.mytracker.ui.utils.io
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding, VM : ViewModel> : Fragment(), CoroutineScope,
    KodeinAware {

    override val kodein by kodein()
    private val factory: MyTrackerViewModelFactory by instance()

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    protected lateinit var binding: T
    protected lateinit var viewModel: VM

    protected val prefs: PreferenceProvider by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        io { MyTrackerApplication.generateAccessToken(prefs) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            getFragmentView(),
            container,
            false
        )
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        return binding.root
    }

    abstract fun getFragmentView(): Int

    abstract fun getViewModel(): Class<VM>

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}