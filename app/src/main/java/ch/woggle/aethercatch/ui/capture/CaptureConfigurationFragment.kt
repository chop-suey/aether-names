package ch.woggle.aethercatch.ui.capture

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import ch.woggle.aethercatch.R


class CaptureConfigurationFragment : Fragment() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 1234

        fun newInstance() =
            CaptureConfigurationFragment()
    }

    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.capture_configuration_fragment, container, false)
        startButton = view.findViewById(R.id.button_start_capturing)
        stopButton = view.findViewById(R.id.button_stop_capturing)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(CaptureConfigurationViewModel::class.java)
        startButton.setOnClickListener { viewModel.startCaptureService(requireContext()) }
        stopButton.setOnClickListener { viewModel.stopCaptureService(requireContext()) }
    }

    override fun onResume() {
        super.onResume()

        // TODO location must be enabled

        if (hasFineLocationPermission()) {
            setButtonsEnabled(true)
        } else {
            setButtonsEnabled(false)
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val index = permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)
            setButtonsEnabled(index >= 0 && grantResults[index] == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        startButton.isEnabled = enabled
        stopButton.isEnabled = enabled
    }

    private fun hasFineLocationPermission() = requireActivity()
        .checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

}