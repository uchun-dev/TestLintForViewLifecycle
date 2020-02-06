package dev.uchun.android.fragment.viewlifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val viewModelFactory = FirstFragmentViewModelFactory()
    private val viewModel: FirstFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FirstFragmentViewModel::class.java)
    }

    private lateinit var textView: TextView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false).also {
            textView = it.findViewById(R.id.textview_first)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment("From FirstFragment")
            findNavController().navigate(action)
        }

        // lint catch here
        viewModel.message.observe(this, Observer {
            textView.text = it
        })
        // TODO: lint didn't catch here
        viewModel.message.observe(this) {
            textView.text = it
        }
    }
}

class FirstFragmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FirstFragmentViewModel() as T
    }
}

class FirstFragmentViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        viewModelScope.launch {
            delay(1_500)
            _message.value = "Hello World"
        }
    }
}