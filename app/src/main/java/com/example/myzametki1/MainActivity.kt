package com.example.myzametki1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val addbut: TextView = findViewById(R.id.addbut)
        addbut.setOnClickListener {
            startActivity(Intent(this@MainActivity,addreport :: class.java))

        }
        var h = Handler()//обновление данных
        var db = Firebase.firestore // Инициализация экземпляра Firestore
        //Добавление слушателя на изменения в базе данных
        db.addSnapshotsInSyncListener {
            updateBD()
        }
        h.postDelayed({ updateBD() }, 200)//Первоначальное обновление данных с задержкой
    }

    private fun updateBD() {
        val recyclerView: RecyclerView = findViewById(R.id.recycleview)
        val db = Firebase.firestore
        db.collection("reports").get().addOnSuccessListener { documents ->
            //Получение данных и формирование списка отчетов
            val reportList = mutableListOf<CustomModel>()
            for (document in documents) {
                val dateMillis = document.getLong("date")
                val formattedDate = if (dateMillis != null) {
                    val date = Date(dateMillis)
                    SimpleDateFormat("dd.MM.yyyy   HH:mm", Locale.getDefault()).format(date)
                } else {
                    "Дата недоступна"
                }
                //Создание объектов CustomModel
                val report = CustomModel(
                    document.id,
                    document.getString("name") ?: "Имя недоступно", //Обработка потенциально null значений
                    document.getString("desktext") ?: "Описание недоступно", //Обработка потенциально null значений
                    formattedDate // Используем отформатированную дату
                )
                reportList.add(report)
            }
            //Сортировка и обновление RecyclerView
            reportList.sortBy { it.date } // Предполагается, что в CustomModel поле date - это String
            reportList.reverse()
            val reportAdapter = CustomAdapter(this@MainActivity, reportList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = reportAdapter
//            // Обработка ошибок
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Не работает БД", Toast.LENGTH_LONG).show()
            Log.w("MainActivity", "Error getting documents:", exception) // Добавлен лог для отладки
        }
    }}