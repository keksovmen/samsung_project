package com.keksovmen.flowerbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.keksovmen.flowerbox.databinding.FragmentTabViewBinding
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.Sensor
import kotlin.reflect.KClass

class DataViewFragment : Fragment() {
    private lateinit var tabs: Array<KClass<out Fragment>>
    private lateinit var binding: FragmentTabViewBinding
    private lateinit var box: FlowerBox
    private lateinit var parent: Nameable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        box = requireArguments().getSerializable(Globals.ARGUMENT_BOX) as FlowerBox
        parent = requireArguments().getSerializable(Globals.ARGUMENT_PARENT) as Nameable
        if (parent is Sensor) {
            tabs = arrayOf(
                SensorDataViewFragment::class,
                SensorDataGraphViewFragment::class
            )
        } else {
            tabs = arrayOf(SwitchDataViewFragment::class)
        }
        binding = FragmentTabViewBinding.inflate(inflater, container, false)

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.setAdapter(ViewAdapter(this))
        TabLayoutMediator(
            view.findViewById(R.id.tab_layout), binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            var label = R.string.list_data_view
            if (position == 1) {
                label = R.string.graph_data_view
            }
            tab.setText(label)
        }.attach()
    }

    inner class ViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int).
            val fragment: Fragment = try {
                tabs[position].java.getDeclaredConstructor().newInstance()
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: java.lang.InstantiationException) {
                throw RuntimeException(e)
            }
            val args = Bundle()
            args.putSerializable(Globals.ARGUMENT_BOX, box)
            args.putSerializable(Globals.ARGUMENT_PARENT, parent)
            fragment.setArguments(args)
            return fragment
        }

        override fun getItemCount(): Int {
            return tabs.size
        }
    }
}
