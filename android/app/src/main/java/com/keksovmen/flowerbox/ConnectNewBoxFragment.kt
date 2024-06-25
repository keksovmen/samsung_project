package com.keksovmen.flowerbox

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.keksovmen.flowerbox.databinding.FragmentConnectBoxBinding
import com.keksovmen.flowerbox.models.ConnectionModel

class ConnectNewBoxFragment : Fragment() {
    private lateinit var binding: FragmentConnectBoxBinding
    private lateinit var waitDialog: AlertDialog
    private lateinit var connectionModel: ConnectionModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConnectBoxBinding.inflate(inflater, container, false)
        waitDialog = WaitDialog.createWaitDialog(requireContext(), getLayoutInflater())
        connectionModel = ViewModelProvider(requireActivity())[ConnectionModel::class.java]
        connectionModel.getStateLiveData()
            .observe(requireActivity()) { state: ConnectionModel.Result -> onStateChanged(state) }

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.connectButton.setOnClickListener(this::connectNewBox)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        waitDialog.dismiss()
    }

    private fun connectNewBox(view: View) {
        val ip = binding.ipInput.getText().toString()
        val port = binding.portInput.getText().toString()
        if (ip.isEmpty() || port.isEmpty()) {
            Log.w(this.javaClass.toString(), "Input is invalid")
            return
        }
        connectionModel.connectNewBox(ip, port.toInt(), requireContext())
    }

    private fun onStateChanged(state: ConnectionModel.Result) {
        when (state) {
            ConnectionModel.Result.NONE -> {}
            ConnectionModel.Result.IN_PROGRESS -> {
                waitDialog.show()
            }

            ConnectionModel.Result.SUCCESS -> {
                Snackbar.make(binding.getRoot(), R.string.success_sync, Snackbar.LENGTH_SHORT)
                    .show()
                requireActivity().finish()
            }

            ConnectionModel.Result.FAILURE -> {
                waitDialog.dismiss()
                Snackbar.make(binding.getRoot(), R.string.error_network, Snackbar.LENGTH_SHORT)
                    .show()
                connectionModel.resetConnection()
            }
        }
    }
}