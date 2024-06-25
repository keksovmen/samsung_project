package com.keksovmen.flowerbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.keksovmen.flowerbox.Globals.getDataBase
import com.keksovmen.flowerbox.Globals.tidToIcon
import com.keksovmen.flowerbox.databinding.FragmentListButtonViewBinding
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import com.keksovmen.flowerbox.entities.Switch
import com.keksovmen.flowerbox.linearList.LinearListAdapter
import com.keksovmen.flowerbox.linearList.LinearListAdapterClickDecorator
import java.util.function.Consumer

class SwitchViewFragment : Fragment() {
    private lateinit var binding: FragmentListButtonViewBinding
    private lateinit var database: FlowerBoxDatabase
    private lateinit var box: FlowerBox


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = getDataBase(requireContext())
        box = requireArguments().getSerializable(Globals.ARGUMENT_BOX) as FlowerBox
        binding = FragmentListButtonViewBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleList.setLayoutManager(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        )
        database.flowerBoxDao().getAllSwitches(box.unique_id).observe(
            getViewLifecycleOwner()
        ) { values: List<Switch> -> onChanged(values) }
        binding.button.setOnClickListener { v: View -> onSyncClicked(v) }
        binding.button.setText(R.string.sync_button)
    }

    private fun onChanged(values: List<Switch>) {
        binding.recycleList.setAdapter(
            LinearListAdapterClickDecorator(
                LinearListAdapter(
                    values, { v: Switch, view: View -> bindView(v, view) },
                    R.layout.list_entry_image_text
                )
            ) { v: Switch -> onListItemClicked(v) })
    }

    private fun bindView(v: Switch, view: View) {
        (view.findViewById<View>(R.id.name) as TextView).text = v.name
        (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(tidToIcon(v.tid))
    }

    private fun onListItemClicked(v: Switch) {
        val bundle = Bundle()
        bundle.putSerializable(Globals.ARGUMENT_BOX, box)
        bundle.putSerializable(Globals.ARGUMENT_PARENT, v)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_BoxView_to_DataView, bundle)
    }

    private fun onSyncClicked(v: View) {
        val api = BoxApi(box.ip, box.port)
        api.fetchAllSwitches(box) { sensors: List<Switch>? ->
            if (sensors == null) {
                Snackbar.make(binding.getRoot(), R.string.error_network, Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                sensors.forEach(Consumer { sensor: Switch ->
                    database.flowerBoxDao().addSwitch(
                        sensor
                    )
                })
                Snackbar.make(binding.getRoot(), R.string.success_sync, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
}