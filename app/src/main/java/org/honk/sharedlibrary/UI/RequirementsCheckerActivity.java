package org.honk.sharedlibrary.UI;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;

import org.honk.sharedlibrary.LocationHelper;
import org.honk.sharedlibrary.R;
import org.honk.sharedlibrary.UIHelper;

public abstract class RequirementsCheckerActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 0;

    private static final int REQUEST_LOCATION_SETTINGS_CHECK = 0;

    public void checkRequirementsAndPermissions(String permission, String feature) {
        // Check requirements for location detection.
        if (!LocationHelper.checkLocationSystemFeature(this, feature)) {
            // The device does not support location detection.
            UIHelper.showAlert(this.getString(R.string.cannotDetectLocation), this);
        } else {
            LocationHelper.checkRequirements(
                    this,
                    (locationSettingsResponse) -> {
                        // Requirements are met: checking user permissions.
                        if (!LocationHelper.checkLocationPermission(this, permission)) {
                            // The user has not given permissions to detect location.
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                                // The user has denied permission: show an alert message.
                                UIHelper.showAlert(this.getString(R.string.permissionDeniedAlertMessage), this, (dialog, id) -> {
                                    this.handlePermissionDeniedMessageClick();
                                });
                            } else {
                                // Request the permission.
                                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_CODE);
                            }
                        }
                    },
                    (e) -> {
                        if (e instanceof ResolvableApiException) {
                            /* Location settings are not satisfied, but this can be fixed
                             * by showing the user a dialog. */
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(this, REQUEST_LOCATION_SETTINGS_CHECK);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Display a message telling the user that the app cannot work.
                                UIHelper.showAlert(this.getString(R.string.cannotDetectLocation), this);
                                this.finishAffinity();
                            }
                        } else {
                            // TODO Allow the user to send feedback.
                            // The exception is not resolvable: display a message telling the user that the app cannot work.
                            UIHelper.showAlert(this.getString(R.string.cannotDetectLocation), this);
                            this.finishAffinity();
                        }
                    });
        }
    }

    /* Defines what should be done when the user accepts the message notifying that the location permission
     * was denied. */
    protected abstract void handlePermissionDeniedMessageClick();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // User denied permission to access location info.
                    UIHelper.showAlert(getString(R.string.permissionDeniedAlertMessage), this);
                    this.finishAffinity();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION_SETTINGS_CHECK) {
            // TODO Handle location settings check result.
        }
    }
}
