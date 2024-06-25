package com.keksovmen.flowerbox

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.keksovmen.flowerbox.Globals.getDataBase
import com.keksovmen.flowerbox.Globals.tidToIcon
import com.keksovmen.flowerbox.WaitDialog.createWaitDialog
import com.keksovmen.flowerbox.databinding.FragmentListButtonViewBinding
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import com.keksovmen.flowerbox.entities.Property
import com.keksovmen.flowerbox.linearList.LinearListAdapter
import com.keksovmen.flowerbox.linearList.LinearListAdapterClickDecorator
import java.util.function.Consumer

class PropertyViewFragment : Fragment() {
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
        database.flowerBoxDao().getAllProperties(box.unique_id).observe(
            getViewLifecycleOwner()
        ) { values: List<Property> -> onChanged(values) }

        binding.button.setOnClickListener { v: View -> onSyncClicked(v) }
    }

    private fun onSyncClicked(v: View) {
        val api = BoxApi(box.ip, box.port)
        api.fetchAllProperties(box) { values: List<Property>? ->
            if (values == null) {
                Snackbar.make(binding.getRoot(), R.string.error_network, Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                values.forEach(Consumer { v: Property -> database.flowerBoxDao().addProperty(v) })
                Snackbar.make(binding.getRoot(), R.string.success_sync, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onChanged(values: List<Property>) {
        binding.recycleList.setAdapter(
            LinearListAdapterClickDecorator(
                LinearListAdapter(
                    values, { v: Property, view: View -> bindView(v, view) },
                    R.layout.list_entry_image_text
                )
            ) { v: Property -> onListItemClicked(v) })
    }

    private fun bindView(v: Property, view: View) {
        (view.findViewById<View>(R.id.name) as TextView).text =
            v.name + " " + v.value

        (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(tidToIcon(v.tid))
    }

    private fun onListItemClicked(v: Property) {
        val dialogView = getLayoutInflater().inflate(R.layout.property_dialog, null)
        (dialogView.findViewById<View>(R.id.value_field) as EditText).setText(v.value.toString())
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(v.name)
            .setMessage(v.description)
            .setView(dialogView)
            .setPositiveButton(R.string.property_change_value) { d: DialogInterface, which: Int ->
                val text =
                    ((d as AlertDialog).findViewById<View>(R.id.value_field) as EditText).getText()
                        .toString()
                val value = text.toFloat()
                if (value < v.min_value || value > v.max_value) {
                    Snackbar.make(
                        binding.getRoot(),
                        R.string.error_property_value,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                d.dismiss()
                val waitDialog = createWaitDialog(requireContext(), getLayoutInflater())
                waitDialog.show()
                BoxApi(box.ip, box.port).setPropertyValue(v, text) { result: Boolean ->
                    if (result) {
                        v.value = value
                        Globals.executor.submit { database.flowerBoxDao().updateProperty(v) }
                        Snackbar.make(
                            binding.getRoot(),
                            R.string.success_sync,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            binding.getRoot(),
                            R.string.error_network,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    waitDialog.dismiss()
                }
            }
            .create()
        dialog.show()
    }
}