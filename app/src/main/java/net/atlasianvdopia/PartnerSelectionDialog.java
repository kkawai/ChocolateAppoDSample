package net.atlasianvdopia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.vdopia.ads.lw.LVDOConstants;

import java.util.List;

public class PartnerSelectionDialog {
    private Context context;
    private List<LVDOConstants.PARTNER> availablePartners;
    private List<LVDOConstants.PARTNER> chosenPartners;

    public PartnerSelectionDialog(Context context, List<LVDOConstants.PARTNER> availablePartners) {
        this.context = context;
        this.availablePartners = availablePartners;
    }

    public void show() {
        //new AlertDialog.Builder().
    }

    public void setListener() {

    }
}
