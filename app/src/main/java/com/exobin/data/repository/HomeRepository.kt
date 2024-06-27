package com.exobin.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HomeRepository @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("tempat_sampah_1/presentaseSampah")
    private val countTrash = database.getReference("/tempat_sampah_1/trashCount")

    fun observeChanges(onDataChange: (Int) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val percentage = dataSnapshot.getValue(Int::class.java)!!
                    onDataChange(percentage)
                    Log.d("FirebaseData", "New percentage: $percentage") // Log the new percentage value
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("HomeRepository", "Failed to read value.", databaseError.toException())
            }
        }

        reference.addValueEventListener(listener)
    }

    fun observeTrashCount(onDataChange: (Int) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val trashCount = dataSnapshot.getValue(Int::class.java)!!
                    onDataChange(trashCount)
                    Log.d("FirebaseData", "New trash count: $trashCount") // Log the new trash count value
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("HomeRepository", "Failed to read value.", databaseError.toException())
            }
        }

        countTrash.addValueEventListener(listener)
    }
}



