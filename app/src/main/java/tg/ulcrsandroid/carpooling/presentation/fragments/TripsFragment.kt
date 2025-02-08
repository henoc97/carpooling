package tg.ulcrsandroid.carpooling.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import tg.ulcrsandroid.carpooling.R
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

class TripsFragment : Fragment(R.layout.fragment_trips) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = view.findViewById(R.id.spinnerConseils)
        val textViewEtapes: TextView = view.findViewById(R.id.textViewEtapes)
        val bouttonUrgences: TextView = view.findViewById(R.id.buttonUrgences)

        val conseils = listOf(
            "Changer un pneu",
            "Que faire en cas d'incendie ?",
            "Comment débloquer les portières ?",
            "Gestes de premiers secours"
        )

        val etapesMap = mapOf(
            "Changer un pneu" to "1. Immobilisez le véhicule\n2. Sortez le cric et la roue de secours\n3. Dévissez les écrous\n4. Remplacez la roue\n5. Revissez et vérifiez la pression",
            "Que faire en cas d'incendie ?" to "1. Coupez le contact\n2. Sortez du véhicule\n3. Alertez les secours\n4. Utilisez un extincteur si possible\n5. Eloignez-vous du danger",
            "Comment débloquer les portières ?" to "1. Vérifiez les serrures\n2. Essayez la clé manuelle\n3. Utilisez un outil fin si nécessaire\n4. Appelez un professionnel si besoin",
            "Gestes de premiers secours" to "1. Assurez-vous que la zone est sécurisée\n2. Vérifiez la respiration de la victime\n3. Appelez les secours\n4. Effectuez un massage cardiaque si nécessaire"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, conseils)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val conseilChoisi = conseils[position]
                textViewEtapes.text = etapesMap[conseilChoisi] ?: ""
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                textViewEtapes.text = ""
            }
        }
        bouttonUrgences.setOnClickListener(this::appelerUrgences)
    }

    private fun appelerUrgences(view: View?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.setData(Uri.parse("tel:118"))
        startActivity(intent)
        Log.d("Carpooling", "TripsFragment:appelerurgences ---> Appeler les urgences")
    }

}