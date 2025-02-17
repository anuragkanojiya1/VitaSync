package com.example.myhealthpassport.graph

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.ViewModels.HealthViewModel
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Composable
fun Graph(){
    val context = LocalContext.current
    val medicalIDs = remember { mutableStateListOf<String>() }
    val healthViewModel: HealthViewModel = viewModel()
    val bloodPressureList = remember { mutableStateListOf<Pair<Int, Int>>() } // Systolic, Diastolic
    val bloodSugarLevelList = remember { mutableStateListOf<Int>() }
    val medications = remember { mutableStateMapOf<String, Int>() } // Medication Name -> Count

    var days = remember { mutableStateOf(0) }

//    LaunchedEffect(Unit) {
//        healthViewModel.fetchMedicalIDs(context) { ids ->
//            medicalIDs.clear()
//            medicalIDs.addAll(ids)
//        }
//    }

    // Fetch Data
    LaunchedEffect(Unit) {
        healthViewModel.fetchMedicalIDs(context) { ids ->
            ids.forEach { id ->
                healthViewModel.retrieveHealthData(id, context) { data ->
                    bloodPressureList.add(Pair(data.systolicBP, data.diastolicBP))
                    bloodSugarLevelList.add(data.bloodSugarLevel)

                    data.medications.split(",").forEach { med ->
                        val trimmedMed = med.trim()
                        medications[trimmedMed] = medications.getOrDefault(trimmedMed, 0) + 1
                    }
                }
            }
        }
    }
    Log.d("Blood Pressure",bloodPressureList.toString())

//    medicalIDs.forEach { id ->
//        healthViewModel.retrieveHealthData(id, context) {
//            bloodPressureList.add(it.bloodPressure)
//            bloodSugarLevelList.add(it.bloodSugarLevel)
//            medications.add(it.medications)
//        }
//    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(
            text = "Health Charts",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))
//
//        // Blood Pressure Chart
//        if (bloodPressureList.isNotEmpty()) {
//            BloodPressureChart(bloodPressureList)
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        // Blood Sugar Chart
//        if (bloodSugarLevelList.isNotEmpty()) {
//            BloodSugarChart(bloodSugarLevelList)
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        // Medications Pie Chart
//        if (medications.isNotEmpty()) {
//            MedicationPieChart(medications)
//        }
    }
}

//
///** 📊 Blood Pressure Line Chart (Systolic & Diastolic) **/
//@Composable
//fun BloodPressureChart(bloodPressureList: List<Pair<Int, Int>>) {
//    val pointsSystolic = bloodPressureList.mapIndexed { index, pair -> Point(index.toFloat(), pair.first.toFloat()) }
//    val pointsDiastolic = bloodPressureList.mapIndexed { index, pair -> Point(index.toFloat(), pair.second.toFloat()) }
//    val minBP = bloodPressureList.minOfOrNull { min(it.first, it.second) } ?: 50
//    val maxBP = bloodPressureList.maxOfOrNull { max(it.first, it.second) } ?: 180
//    val stepCount = 4  // Number of labels on Y-axis
//    val stepSize = (maxBP - minBP) / stepCount
//    val lineChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(
//                Line(pointsDiastolic, LineStyle(color = Color.Red),
//                    IntersectionPoint(),
//                    SelectionHighlightPoint(),
//                    ShadowUnderLine()),
//                Line(pointsSystolic, LineStyle(color = Color.Blue),
//                    IntersectionPoint(),
//                    SelectionHighlightPoint(),
//                    ShadowUnderLine()),
//
//                )
//        ),
//        xAxisData = AxisData.Builder()
//            .axisStepSize(60.dp)
//            .backgroundColor(Color.Yellow)
//            .steps(pointsSystolic.size - 1)
//            .labelData { i -> "${i + 1}" }
//            .labelAndAxisLinePadding(15.dp)
//            .build(),
//        yAxisData = AxisData.Builder()
//            .steps(stepCount)
//            .labelData { i -> "${(minBP + (i * stepSize)).toInt()} mmHg" }
//            .axisLineColor(Color.White)
//            .labelAndAxisLinePadding(20.dp)
//            .build(),
//        gridLines = GridLines(),
//        backgroundColor = Color.Green
//    )
//
//    Column {
//        Text("Blood Pressure Chart (Systolic & Diastolic)", style = MaterialTheme.typography.bodyLarge)
//        LineChart(
//            modifier = Modifier.fillMaxWidth().height(300.dp),
//            lineChartData = lineChartData
//        )
//    }
//}
//
///** 📊 Blood Sugar Line Chart **/
//@Composable
//fun BloodSugarChart(bloodSugarLevelList: List<Int>) {
//    val points = bloodSugarLevelList.mapIndexed { index, level -> Point(index.toFloat(), level.toFloat()) }
//
//    val lineChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(Line(points, LineStyle(color = Color.Green)))
//        ),
//        xAxisData = AxisData.Builder()
//            .steps(points.size - 1)
//            .labelData { i -> "Day ${i + 1}" }
//            .build(),
//        yAxisData = AxisData.Builder()
//            .steps(5)
//            .labelData { i -> "${i * 40} mg/dL" }
//            .build()
//    )
//
//    Column {
//        Text("Blood Sugar Levels Over Time", style = MaterialTheme.typography.bodyLarge)
//        LineChart(
//            modifier = Modifier.fillMaxWidth().height(300.dp),
//            lineChartData = lineChartData
//        )
//    }
//}
//
///** 🥧 Medications Pie Chart **/
//@Composable
//fun MedicationPieChart(medications: Map<String, Int>) {
//    val pieSlices = medications.map { PieChartData.Slice(it.key, it.value.toFloat(), getRandomColor()) }
//
//    val pieChartData = PieChartData(slices = pieSlices, plotType = PlotType.Line)
//    val pieChartConfig = PieChartConfig(
//        //percentVisible = true,
//        isAnimationEnable = true,
//        showSliceLabels = true,
//        animationDuration = 1500
//    )
//
//    Column {
//        Text("Medication Usage Distribution", style = MaterialTheme.typography.bodyLarge)
//        PieChart(
//            modifier = Modifier.fillMaxSize(),
//            pieChartData = pieChartData,
//            pieChartConfig = pieChartConfig
//        )
//    }
//}
//
///** 🔵 Get Random Colors for Pie Chart Slices **/
//fun getRandomColor(): Color {
//    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
//    return colors.random()
//}
