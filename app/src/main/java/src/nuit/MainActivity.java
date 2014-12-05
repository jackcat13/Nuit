package src.nuit;

import android.app.Activity;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Service;
import model.Type_personne;


public class MainActivity extends Activity {

    private ServiceAsyncRecherche mAsyncTask;
    private ArrayList<Type_personne> listTypesPersonne;

    private boolean isFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listTypesPersonne = new ArrayList<Type_personne>();

        LinearLayout ll = (LinearLayout) findViewById(R.id.lolLayout);

        //Initialisation de la tache asynchrone
        mAsyncTask = new ServiceAsyncRecherche();

        //Lancement de la tâche asynchrone
        mAsyncTask.execute();

        while (isFull == false);

        for (Type_personne t: listTypesPersonne ){
            TextView text = new TextView(this);
            text.setText(t+"");
            this.addContentView(text, ll.getLayoutParams());
        }
    }

    //Tache asynchrone permettant de lancer une requette http au webservice
    private class ServiceAsyncRecherche extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            String donneesUser;
            String error;
            JSONArray array;
            Object obj;
            JSONParser parser = new JSONParser();
            JSONArray donnees = new JSONArray();

            try {
                donneesUser = Service.getResult("?select=1&requete=1");//Envoie de la requete au serviceweb
                array = (JSONArray) parser.parse(donneesUser); //Parse le résultat de la requete dans un tableau d'objets JSON
                System.out.println(array);

                for (int i = 0; i < array.size(); i++) {
                    listTypesPersonne.add(new Type_personne()); //Crée un utilisateur dans une liste pour chaque utilisateur de la requete

                    //Instancie l'utilisateur crée avec les informations issues de la requête
                    listTypesPersonne.get(i).setIdType_personne(Integer.parseInt((String) ((JSONObject) array.get(i)).get("idTypePersonne")));
                    listTypesPersonne.get(i).setLibelleType_personne((String) ((JSONObject) array.get(i)).get("LIBELLETYPEPERSONNE"));
                }
                isFull = true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    } // ServiceAsync ();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
