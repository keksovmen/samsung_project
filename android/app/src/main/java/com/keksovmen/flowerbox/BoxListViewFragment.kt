package com.keksovmen.flowerbox

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.keksovmen.flowerbox.Globals.getDataBase
import com.keksovmen.flowerbox.databinding.FragmentListButtonViewBinding
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import com.keksovmen.flowerbox.linearList.BindableViewHolderIface
import com.keksovmen.flowerbox.linearList.LinearListAdapter
import com.keksovmen.flowerbox.linearList.LinearListAdapterClickDecorator
import com.keksovmen.flowerbox.linearList.LinearListHolderDecorator

class BoxListViewFragment : Fragment() {
    private lateinit var binding: FragmentListButtonViewBinding
    private lateinit var database: FlowerBoxDatabase
    private var touchedId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = getDataBase(requireContext())
        binding = FragmentListButtonViewBinding.inflate(inflater, container, false)

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleList.setLayoutManager(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        )
        binding.recycleList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        database.flowerBoxDao().getAllBoxes().observe(
            getViewLifecycleOwner()
        ) { flowerBoxes: List<FlowerBox> -> onChanged(flowerBoxes) }
        binding.button.setOnClickListener { v: View? ->
            startActivity(Intent(requireContext(), AddNewBoxActivity::class.java))
        }
        binding.button.setText(R.string.connect_button)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().getMenuInflater().inflate(R.menu.item_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            val box =
                (binding.recycleList.adapter as LinearListHolderDecorator<FlowerBox>).data[touchedId]
            Globals.executor.submit { database.flowerBoxDao().deleteFlowerBox(box) }
            return true
        }

        return super.onContextItemSelected(item)
    }

    private fun onChanged(flowerBoxes: List<FlowerBox>) {
        binding.recycleList.setAdapter(
            LinearListHolderDecorator(
                LinearListAdapterClickDecorator(
                    LinearListAdapter(
                        flowerBoxes, { v: FlowerBox, view: View -> bindView(v, view) },
                        R.layout.list_entry_image_text
                    )
                ) { v: FlowerBox -> onListItemClicked(v) }
            ) { holder: BindableViewHolderIface<FlowerBox>, integer: Int ->
                holder.itemView.setOnTouchListener { v: View?, event: MotionEvent? ->
                    touchedId = integer
                    false
                }
                registerForContextMenu(holder.itemView)
            })
    }

    private fun bindView(v: FlowerBox, view: View) {
        (view.findViewById<View>(R.id.name) as TextView).text = v.name + " " + v.unique_id
        val res = intArrayOf(
            R.drawable.flower_1,
            R.drawable.flower_2,
            R.drawable.flower_3,
            R.drawable.flower_4,
            R.drawable.flower_5
        )
        (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(
            res[v.unique_id % res.size]
        )
    }

    private fun onListItemClicked(v: FlowerBox) {
        val bundle = Bundle()
        bundle.putSerializable(Globals.ARGUMENT_BOX, v)
        NavHostFragment.findNavController(this@BoxListViewFragment)
            .navigate(R.id.action_BoxListView_to_BoxView, bundle)
    }
}