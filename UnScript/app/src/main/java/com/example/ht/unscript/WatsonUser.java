package com.example.ht.unscript;

import android.graphics.Bitmap;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by CGT on 10-02-2018.
 */

public class WatsonUser implements IChatUser {

    Integer id;
    String name;
    Bitmap icon;

    public WatsonUser(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getId() {
        return id.toString();
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Bitmap bitmap) {
        this.icon = icon;
    }
}
