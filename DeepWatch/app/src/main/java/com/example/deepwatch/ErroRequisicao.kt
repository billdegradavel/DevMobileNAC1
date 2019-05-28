package com.example.deepwatch

import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError

public class ErroRequisicao : Response.ErrorListener {

    override fun onErrorResponse(error: VolleyError?) {
        Log.e("ERRO VOLLEY", "Erro Requisição: "+error?.message)
        Log.d("ERROR DETAILS?",error.toString())
        error?.networkResponse.toString()
//        largeLog("LARGE LOG", error?.message)
    }

//    fun largeLog(tag: String, content: String?) {
//        val size :Int = content.isNotEmpty() ?: content?.length
//        if (content?.length > 4000) {
//            Log.d(tag, content?.substring(0, 4000))
//            largeLog(tag, content?.substring(4000))
//        } else {
//            Log.d(tag, content)
//        }
//    }
}