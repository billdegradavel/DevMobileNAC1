package com.example.deepwatch

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.security.MessageDigest
import java.util.regex.Pattern
import java.net.URLEncoder

class LoginActivity : AppCompatActivity() {

    lateinit var campologin : EditText
    lateinit var camposenha : EditText
    lateinit var btnLogin : Button
    lateinit var loginTxt : TextView
    lateinit var responseTxt : TextView

    private val validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    fun isValidEmail(emailStr : String) : Boolean {
        val matcher = validEmailAddressRegex.matcher(emailStr)
        return matcher.find()
    }

    private val atLeastOneNumberPattern = Pattern.compile(".*[0-9].*")
    fun isValidSenha(password: String): Boolean {
        if (password.length < 8) {
            return false
        }
        if (password.length > 90) {
            return false
        }
        if (!atLeastOneNumberPattern.matcher(password).find()) {
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        campologin = findViewById(R.id.campologin)
        camposenha = findViewById(R.id.camposenha)
        btnLogin = findViewById(R.id.btnLogin)
        loginTxt = findViewById(R.id.logintxt)
        responseTxt = findViewById(R.id.responseTxt)

        campologin.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    responseTxt.text = getString(R.string.emailInvalido)
                    responseTxt.setTextColor(ContextCompat.getColor(responseTxt.context, R.color.red))
                }
                else{
                    responseTxt.setTextColor(ContextCompat.getColor(responseTxt.context, R.color.transparent))
                }
            }
        })

        camposenha.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isValidSenha(s.toString())){
                    responseTxt.text = getString(R.string.senhaInvalida)
                    responseTxt.setTextColor(ContextCompat.getColor(responseTxt.context, R.color.red))
                }
                else{
                    responseTxt.setTextColor(ContextCompat.getColor(responseTxt.context, R.color.transparent))
                }
            }
        })
    }

    public fun tentarLogin(v: View){
        var login:String = campologin.text.toString()
        var senha:String = camposenha.text.toString()
        var senhaHash = toHash(senha)

        var user : JSONObject = JSONObject()
        user.put("valLogin","valLogin")
        user.put("login", login)
        user.put("senha", senhaHash)
//        loginTxt.text = user.toString()
//        campologin.text = loginTxt.editableText
        val url = "https://www.bastillear.com/php/Query.php"
        val queue = Volley.newRequestQueue(this)
        var erroReq : ErroRequisicao = ErroRequisicao()
        val jsonObjReq = JsonObjectRequest(Request.Method.POST, url,user,Response.Listener<JSONObject> { response ->
            Log.d("RETURN", "RETURN FROM PHP")
            Log.d("login_id",response.getInt("login_id").toString())
//            loginTxt.text = "COME BACK"
            var result:Int? = response.getInt("login_id")
//            loginTxt.text = result.toString()
            if (result != null){
                logar(v)
            }
            if (result == null){
                erroLogin()
            }
        }, Response.ErrorListener { error ->
            Log.e("ERRO VOLLEY", "Erro Requisição: "+ error?.message)
//            Log.d("ERROR DETAILS?", error.toString())
//            Log.e("ERRO PT1", "Erro Pt1: "+ error?.message?.subSequence(0,25))
//            Log.e("ERRO PT2", "Erro Pt2: "+ error?.message?.subSequence(25,50))
//            Log.e("ERRO PT3", "Erro Pt3: "+ error?.message?.subSequence(50,76))
        })
        queue.add(jsonObjReq)
//        loginTxt.text = "WAIT NOW"
    }

    public fun toHash(txtPuro : String) : String{
        var txthash:String = ""

        var digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        digest.update(txtPuro.toByteArray())
        var senhaHash = digest.digest()

        val hexString = StringBuffer()
        for (i in 0 until senhaHash.size)
            hexString.append(Integer.toHexString(0xFF and senhaHash[i].toInt()))

        txthash = String(hexString)
        return txthash
    }

    public fun logar(view: View){
        val intent = Intent(this, MainActivity::class.java).apply { putExtra(EXTRA_MESSAGE, "teste") }
        startActivity(intent)
    }

    public fun erroLogin(){
        responseTxt.text = getString(R.string.erroLogin)
        responseTxt.setTextColor(ContextCompat.getColor(responseTxt.context, R.color.red))
    }

    fun cadastrar(){
        var x = "{}";
    }
}
