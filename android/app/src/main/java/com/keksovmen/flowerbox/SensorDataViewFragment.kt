package com.keksovmen.flowerbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.keksovmen.flowerbox.Globals.getDataBase
import com.keksovmen.flowerbox.databinding.FragmentListButtonViewBinding
import com.keksovmen.flowerbox.entities.DataEntry
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import com.keksovmen.flowerbox.entities.Sensor
import com.keksovmen.flowerbox.linearList.BindableViewHolderIface
import com.keksovmen.flowerbox.linearList.LinearListAdapter
import com.keksovmen.flowerbox.linearList.LinearListHolderDecorator
import java.util.Date
import java.util.function.Consumer

class SensorDataViewFragment : Fragment() {
    private lateinit var binding: FragmentListButtonViewBinding
    private lateinit var database: FlowerBoxDatabase
    private lateinit var box: FlowerBox
    private lateinit var parent: Sensor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = getDataBase(requireContext())
        box = requireArguments().getSerializable(Globals.ARGUMENT_BOX) as FlowerBox
        parent = requireArguments().getSerializable(Globals.ARGUMENT_PARENT) as Sensor
        binding = FragmentListButtonViewBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleList.setLayoutManager(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        )
        database.flowerBoxDao().getAllSensorData(parent.parent_id, parent.id).observe(
            getViewLifecycleOwner()
        ) { values: List<DataEntry> -> onChanged(values) }

        binding.button.setOnClickListener { _ignore: View -> onSyncClicked(_ignore) }
        binding.button.setText(R.string.sync_button)
    }

    private fun onSyncClicked(_ignore: View) {
        Globals.executor.submit {
            val timestamp = database.flowerBoxDao().getLastSensorData(parent.parent_id, parent.id)
            val time = (timestamp?.timestamp ?: (Date().time / 1000)) - 60 * 60 * 24
            val api = BoxApi(box.ip, box.port)
            api.fetchSensorData(parent, time) { vals: List<DataEntry>? ->
                if (vals == null) {
                    Snackbar.make(binding.getRoot(), R.string.error_network, Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    vals.forEach(Consumer { v: DataEntry ->
                        database.flowerBoxDao().addSensorData(v)
                    })

                    Snackbar.make(binding.getRoot(), R.string.success_sync, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun onChanged(values: List<DataEntry>) {
        binding.recycleList.setAdapter(LinearListHolderDecorator(
            LinearListAdapter(
                values,
                { (value, timestamp): DataEntry, view: View ->
                    (view.findViewById<View>(R.id.date) as TextView).text =
                        Globals.formatDataView.format(Date(timestamp * 1000))

                    (view.findViewById<View>(R.id.value) as TextView).text = value.toString()
                },
                R.layout.list_entry_text
            )
        ) { holder: BindableViewHolderIface<DataEntry>, position: Int ->
            holder.itemView.setBackgroundColor(
                resources.getColor(
                    if (position % 2 == 0) R.color.md_theme_surfaceContainerHigh else R.color.md_theme_surfaceContainerLow,
                    null
                )
            )
        })
    }
}