package com.keksovmen.flowerbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.keksovmen.flowerbox.Globals.getDataBase
import com.keksovmen.flowerbox.databinding.FragmentSensorDataGraphViewBinding
import com.keksovmen.flowerbox.entities.DataEntry
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import com.keksovmen.flowerbox.entities.Sensor
import java.util.Date

class SensorDataGraphViewFragment : Fragment() {
    private lateinit var binding: FragmentSensorDataGraphViewBinding
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
        binding = FragmentSensorDataGraphViewBinding.inflate(inflater, container, false)

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Globals.executor.submit {
            val lastSensorData =
                database.flowerBoxDao().getLastSensorData(parent.parent_id, parent.id)
            val endTime = lastSensorData?.timestamp ?: (Date().time / 1000)
            val startTime = endTime - 60 * 60 * 24

            requireActivity().runOnUiThread {
                database.flowerBoxDao()
                    .getSensorDataOfPeriod(parent.parent_id, parent.id, startTime, endTime)
                    .observe(
                        getViewLifecycleOwner()
                    ) { values: List<DataEntry> ->
                        val entries: MutableList<Entry> = ArrayList(values.size)
                        for (i in values.indices) {
                            entries.add(Entry(i.toFloat(), values[i].value))
                        }
                        val dataSet = LineDataSet(entries, parent.name)
                        dataSet.setColor(resources.getColor(R.color.md_theme_primary, null))
                        dataSet.setCircleColor(
                            resources.getColor(
                                R.color.md_theme_primary_mediumContrast,
                                null
                            )
                        )
                        dataSet.setValueTextColor(
                            resources.getColor(
                                R.color.md_theme_tertiary,
                                null
                            )
                        )
                        dataSet.setLineWidth(2f)
                        dataSet.circleRadius = 6f
                        dataSet.valueTextSize = 10f
                        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return Globals.formatTimeView.format(Date(values[value.toInt()].timestamp * 1000))
                            }
                        }
                        binding.chart.setData(LineData(dataSet))
                        binding.chart.invalidate()
                    }
            }
        }
    }
}