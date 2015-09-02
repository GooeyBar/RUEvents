package gtsioni.dtsioni.crejaud.ruevents;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private JSONObject jsonObj;

    private ListView eventsListView;
    private List<Event> eventArr = new ArrayList<Event>();
    private ArrayAdapter<Event> eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventsListView = (ListView) findViewById(R.id.eventsListView);

        // call api to get json of events
        new getEvents().execute();

    }

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

    private void showEvents() throws JSONException {

        //parse through json and set attributes in arraylists
        //attributes: name, description, location, host, datetime, id

        JSONArray recordsArr = jsonObj.getJSONArray("Records");

        for (int i = 0; i < recordsArr.length(); i++)
            eventArr.add(new Event(recordsArr.getJSONObject(i).getString("Name"),
                    recordsArr.getJSONObject(i).getString("Description"),
                    recordsArr.getJSONObject(i).getString("Location"),
                    recordsArr.getJSONObject(i).getString("Host"),
                    recordsArr.getJSONObject(i).getLong("Date")));


        eventsAdapter = new eventListAdapter();
        eventsListView.setAdapter(eventsAdapter);
    }

    private class eventListAdapter extends ArrayAdapter<Event> {

        public eventListAdapter() {
            super(MainActivity.this, R.layout.event_list_item, eventArr);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.event_list_item, parent, false);
            TextView nameTextView, locationTextView, hostTextView, descriptionTextView, timeTextView;

            nameTextView = (TextView) view.findViewById(R.id.name);
            locationTextView = (TextView) view.findViewById(R.id.location);
            hostTextView = (TextView) view.findViewById(R.id.host);
            descriptionTextView = (TextView) view.findViewById(R.id.description);
            timeTextView = (TextView) view.findViewById(R.id.time);

            nameTextView.setText(eventArr.get(position).getName());
            locationTextView.setText(eventArr.get(position).getLocation());
            hostTextView.setText(eventArr.get(position).getHost());
            descriptionTextView.setText(eventArr.get(position).getDescription());
            timeTextView.setText(eventArr.get(position).getDate().toString());

            return view;
        }
    }

    private class getEvents extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... p) {
            ApiServiceHandler apiSH = new ApiServiceHandler();

            String url = getResources().getString(R.string.endpoint) + getResources().getString(R.string.api_events);
            String jsonStr = apiSH.makeServiceCall(url, ApiServiceHandler.GET, null);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                showEvents();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
