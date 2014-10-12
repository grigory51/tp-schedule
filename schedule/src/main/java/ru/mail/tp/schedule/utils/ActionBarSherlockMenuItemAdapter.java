package ru.mail.tp.schedule.utils;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/**
* @author dimasokol
*/
public class ActionBarSherlockMenuItemAdapter implements MenuItem {
    private final com.actionbarsherlock.view.MenuItem item;

    public ActionBarSherlockMenuItemAdapter(com.actionbarsherlock.view.MenuItem item) {
        this.item = item;
    }

    @Override
    public int getItemId() {
        return item.getItemId();
    }

    public boolean isEnabled() {
        return item.isEnabled();
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public View getActionView() {
        return item.getActionView();
    }

    @Override
    public char getAlphabeticShortcut() {
        return item.getAlphabeticShortcut();
    }

    @Override
    public int getGroupId() {
        return item.getGroupId();
    }

    @Override
    public Drawable getIcon() {
        return item.getIcon();
    }

    @Override
    public Intent getIntent() {
        return item.getIntent();
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return item.getMenuInfo();
    }

    @Override
    public char getNumericShortcut() {
        return item.getNumericShortcut();
    }

    @Override
    public int getOrder() {
        return item.getOrder();
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return item.getTitle();
    }

    @Override
    public CharSequence getTitleCondensed() {
        return item.getTitleCondensed();
    }

    @Override
    public boolean hasSubMenu() {
        return item.hasSubMenu();
    }

    @Override
    public boolean isActionViewExpanded() {
        return item.isActionViewExpanded();
    }

    @Override
    public boolean isCheckable() {
        return item.isCheckable();
    }

    @Override
    public boolean isChecked() {
        return item.isChecked();
    }

    @Override
    public boolean isVisible() {
        return item.isVisible();
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException("setActionProvider() is not implemented");
    }

    @Override
    public MenuItem setActionView(View view) {
        item.setActionView(view);
        return this;
    }

    @Override
    public MenuItem setActionView(int resId) {
        item.setActionView(resId);
        return this;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        item.setAlphabeticShortcut(alphaChar);
        return this;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        item.setCheckable(checkable);
        return this;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        item.setChecked(checked);
        return this;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        item.setEnabled(enabled);
        return this;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        item.setIcon(icon);
        return this;
    }

    @Override
    public MenuItem setIcon(int iconRes) {
        item.setIcon(iconRes);
        return this;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        item.setIntent(intent);
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        item.setNumericShortcut(numericChar);
        return this;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        throw new UnsupportedOperationException("setOnActionExpandListener() is not implemented");
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        throw new UnsupportedOperationException("setOnMenuItemClickListener() is not implemented");
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        item.setShortcut(numericChar, alphaChar);
        return this;
    }

    @Override
    public void setShowAsAction(int actionEnum) {
        item.setShowAsAction(actionEnum);
    }

    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        item.setShowAsActionFlags(actionEnum);
        return this;
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        item.setTitle(title);
        return this;
    }

    @Override
    public MenuItem setTitle(int title) {
        item.setTitle(title);
        return this;
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence title) {
        item.setTitleCondensed(title);
        return this;
    }

    @Override
    public MenuItem setVisible(boolean visible) {
        item.setVisible(visible);
        return this;
    }
}
