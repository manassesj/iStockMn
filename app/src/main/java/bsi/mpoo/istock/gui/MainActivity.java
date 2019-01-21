package bsi.mpoo.istock.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.client.ClientsActivity;
import bsi.mpoo.istock.gui.fragments.HomeFragment;
import bsi.mpoo.istock.gui.historic.HistoricActivity;
import bsi.mpoo.istock.gui.product.ProductsActivity;
import bsi.mpoo.istock.gui.sales.ProductsOrderActivity;
import bsi.mpoo.istock.gui.user.SettingsActivity;
import bsi.mpoo.istock.gui.user.UsersActivity;
import bsi.mpoo.istock.services.ImageServices;
import bsi.mpoo.istock.services.SessionServices;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    byte[] image;
    private TextView textViewCompany;
    private TextView textViewName;
    private ImageView companyImageView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        textViewName = headerView.findViewById(R.id.textViewNameUserHeaderHome);
        textViewCompany = headerView.findViewById(R.id.textViewCompanyHeaderHome);
        companyImageView = headerView.findViewById(R.id.imageViewHeader);
        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Menu nav_Menu = navigationView.getMenu();
        if (Session.getInstance().getAccount() instanceof Administrator){
            textViewName.setText(((Administrator) Session.getInstance().getAccount()).getUser().getName());
            textViewCompany.setText(((Administrator) Session.getInstance().getAccount()).getUser().getCompany());
            image = ((Administrator) Session.getInstance().getAccount()).getUser().getImage();
        } else if (Session.getInstance().getAccount() instanceof Salesman){
            textViewName.setText(((Salesman) Session.getInstance().getAccount()).getUser().getName());
            textViewCompany.setText(((Salesman) Session.getInstance().getAccount()).getUser().getCompany());
            image = ((Salesman) Session.getInstance().getAccount()).getUser().getImage();
            nav_Menu.findItem(R.id.nav_users).setVisible(false);
            nav_Menu.findItem(R.id.nav_products).setVisible(false);
        } else {
            textViewName.setText(((Producer) Session.getInstance().getAccount()).getUser().getName());
            textViewCompany.setText(((Producer) Session.getInstance().getAccount()).getUser().getCompany());
            image = ((Producer) Session.getInstance().getAccount()).getUser().getImage();
            nav_Menu.findItem(R.id.nav_sales).setVisible(false);
            nav_Menu.findItem(R.id.nav_users).setVisible(false);
            nav_Menu.findItem(R.id.nav_historic).setVisible(false);
            nav_Menu.findItem(R.id.nav_clients).setVisible(false);
        }

        if (image != null){
            ImageServices imageServices = new ImageServices();
            setImageOnImageView(companyImageView, imageServices);

        }
    }

    private void setImageOnImageView(ImageView companyImageView, ImageServices imageServices) {
        companyImageView.setImageBitmap(imageServices.byteToImage(image));
        companyImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    public void startAActivity(Class Activity){
        Intent intent = new Intent(getApplicationContext(), Activity );
        startActivity(intent);

    }

    public void displaySelectedScreen(int id){
        Fragment fragment = null;
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButton);
        if (id == R.id.nav_home){
            fragment = new HomeFragment();
            floatingActionButton.hide();
        } else if (id == R.id.nav_sales){
            startAActivity(ProductsOrderActivity.class);
        } else if (id == R.id.nav_products){
            startAActivity(ProductsActivity.class);
        } else if (id == R.id.nav_clients) {
            startAActivity(ClientsActivity.class);
        } else if (id == R.id.nav_users) {
            startAActivity(UsersActivity.class);
        } else if (id == R.id.nav_historic) {
            startAActivity(HistoricActivity.class);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            SessionServices sessionServices = new SessionServices(getApplicationContext());
            sessionServices.clearSession();
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
