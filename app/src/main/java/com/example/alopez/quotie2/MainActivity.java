package com.example.alopez.quotie2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Random;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class MainActivity extends AppCompatActivity {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.news_articles);
//    }
    BranchUniversalObject branchUniversalObject;
    final String[] quotes = {"Cows", "Pigs", "Frogs", "Squirrels"};
    Branch branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateQuote();

        final TextView changingQuote = (TextView) findViewById(R.id.quote);

        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("My Content Title")
                .setContentDescription("My Content Description")
                .setContentImageUrl("https://lorempixel.com/400/400")
                .addContentMetadata("custom_data", "123");

        findViewById(R.id.refresh_quote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuote();
            }
        });
        shareLink();
        setUserId();
        //refreshRewards();
        triggerCustomEvent();
        hyperlinkButton();
    }

    @Override
    public void onStart() {
        super.onStart();
        branch = Branch.getInstance();
        branch.initSession(new Branch.BranchReferralInitListener(){
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {

                    Log.i("REFERRING PARAMS", referringParams.toString());
                    String quoteID = referringParams.optString("quote", "");
                    if (quoteID.equals("")) {
                        updateQuote();
                    } else {
                        final TextView changingQuote = (TextView) findViewById(R.id.quote);
                        changingQuote.setText(quoteID);
                    }
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                } else {
                    Log.i("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    private void updateQuote() {
        final TextView quote = (TextView) findViewById(R.id.quote);
        int rand = new Random().nextInt(quotes.length);
        String randQuote = quotes[rand];
        branchUniversalObject = new BranchUniversalObject()
                .setTitle("Random Quotes")
                .setContentDescription("quotes")
                .addContentMetadata("quote", randQuote)
                .setCanonicalIdentifier("content/12345")
                .setContentDescription("My Content Description")
                .setContentImageUrl("https://lorempixel.com/400/400")
                .addContentMetadata("custom_data", "123");
        quote.setText(randQuote);
    }

    public void shareLink() {
        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("myShareChannel2");

                ShareSheetStyle shareSheetStyle = new ShareSheetStyle(MainActivity.this, "Check this out!", "This stuff is awesome: ")
                        .setSharingTitle("Share With");

                branchUniversalObject.showShareSheet(MainActivity.this, linkProperties, shareSheetStyle, new Branch.BranchLinkShareListener() {

                            @Override
                            public void onShareLinkDialogLaunched() {
                            }

                            @Override
                            public void onShareLinkDialogDismissed() {
                            }

                            @Override
                            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                            }

                            @Override
                            public void onChannelSelected(String channelName) {
                            }

                        },
                        new Branch.IChannelProperties() {
                            @Override
                            public String getSharingTitleForChannel(String channel) {
                                return channel.contains("Messaging") ? "title for SMS" :
                                        channel.contains("Slack") ? "title for slack" :
                                                channel.contains("Gmail") ? "title for gmail" : null;
                            }

                            @Override
                            public String getSharingMessageForChannel(String channel) {
                                return channel.contains("Messaging") ? "message for SMS" :
                                        channel.contains("Slack") ? "message for slack" :
                                                channel.contains("Gmail") ? "message for gmail" : null;
                            }
                        });

            }
        });
    }

    public void setUserId() {
        Button setUserIdButton = (Button) findViewById(R.id.setuseridbutton);
        final EditText userId = (EditText) findViewById(R.id.userid);
        setUserIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                branch.setIdentity(userId.getText().toString());
                refreshRewards();
            }
        });
    }

    public void triggerCustomEvent() {
        Button customEventButton = (Button) findViewById(R.id.customeventbutton);
        customEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.userCompletedAction("triggered Custom Event");
                Log.d("STATE", "blahblah");
//                Log.d("STATE", "blahblah");
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) "https://9wlb.app.link/VULxhpQhxH"));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setPackage("com.android.chrome");
//                startActivity(intent);
            }
        });
    }

    public void refreshRewardsButton() {
        Button refreshButton = (Button) findViewById(R.id.refreshrewards);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshRewards();
            }
        });
    }

    public void hyperlinkButton() {
        Button myhyperlink = (Button) findViewById(R.id.hyperlinkbutton);
        myhyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("STATE", "blahblah");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) "https://9wlb.app.link/hRXAHkUlxH"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                startActivity(intent);
            }
        });
    }


    private void refreshRewards() {
        branch.loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean changed, BranchError error) {
                TextView rewards = (TextView) findViewById(R.id.rewards);
                rewards.setText(""+ branch.getCredits());
                Log.d("STATE", rewards.getText().toString());
            }
        });
    }
}
