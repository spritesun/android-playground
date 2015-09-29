package com.sunlong.first;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sunlong.first.datasource.DataSource;
import com.sunlong.first.datasource.DataSourceItem;

public class MainActivity extends ActionBarActivity {

    private ListView mListView;
    private static int CREATE_QUOTE_ID = 1;

    public class QuoteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataSource.getDataSourceLength();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView thumbnail;
            TextView quote;

            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.list_item_layout, parent, false);
            }

            thumbnail = (ImageView) convertView.findViewById(R.list.thumb);
            thumbnail.setImageBitmap(mDataSource.getmItemsData().get(position).getmThumbnail());
            quote = (TextView) convertView.findViewById(R.list.text);
            quote.setText(mDataSource.getmItemsData().get(position).getmQuote());

            return convertView;
        }

        private Context mContext;
        private LayoutInflater mInflator;
        private DataSource mDataSource;

        public QuoteAdapter(Context c) {
            mContext = c;
            mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDataSource = DataSource.getDataSourceInstance(mContext);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.planetSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planetNames, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        mListView = (ListView) findViewById(R.id.quotes_list);
        mListView.setAdapter(new QuoteAdapter(this));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int position,
                                    long arg3) {
                Intent i = new Intent(MainActivity.this, QuoteDetail.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });

        registerForContextMenu(mListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            QuoteAdapter adapter = (QuoteAdapter) mListView.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(Menu.NONE, CREATE_QUOTE_ID, Menu.NONE, "Create new quote");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == CREATE_QUOTE_ID) {
            //create an entry into the data set
            //and call data set changed
            DataSource dataSource = DataSource.getDataSourceInstance(this);
            dataSource.getmItemsData().add(new DataSourceItem());
            QuoteAdapter adapter = (QuoteAdapter) mListView.getAdapter();
            adapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getResources().getString(R.string.context_menu_title));
        MenuInflater menuInflator = getMenuInflater();
        menuInflator.inflate(R.layout.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.delete) {
            DataSource.getDataSourceInstance(this).getmItemsData().remove(info.position);
            QuoteAdapter adapter = (QuoteAdapter) mListView.getAdapter();
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
