package com.example.academate.data.repository

import com.example.academate.data.model.MataKuliahModelResponse
import com.example.academate.model.MataKuliah
import com.example.academate.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MataKuliahRepository {
    private var db: DatabaseReference =
        FirebaseDatabase
            .getInstance("https://academate-c7250-default-rtdb.firebaseio.com/")
            .reference
            .child("MataKuliah")

    var currentMatkul: MataKuliahModelResponse? = null

    fun getMataKuliah(): Flow<Resource<List<MataKuliahModelResponse>>> =
        callbackFlow {
            trySend(Resource.Loading())

            db.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.map {
                        MataKuliahModelResponse(it.getValue(MataKuliah::class.java), it.key)
                    }
                    trySend(Resource.Success(items))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Resource.Error("Tidak bisa mengambil data \n${error.message}"))
                }
            })

            awaitClose {
                close()
            }
        }

    fun getMatkulById(id: String) =
        callbackFlow<Resource<MataKuliahModelResponse>> {
            trySend(Resource.Loading())

            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val matkul = snapshot.children.map {
                        MataKuliahModelResponse(it.getValue(MataKuliah::class.java), it.key)
                    }.filter { it.key == id }
                    currentMatkul = matkul[0]
                    trySend(Resource.Success(matkul[0]))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Resource.Error(error.toString()))
                }
            })

            awaitClose {
                close()
            }
        }
}