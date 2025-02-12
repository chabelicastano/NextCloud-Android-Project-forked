/*
 *
 * Nextcloud Android client application
 *
 * @author Tobias Kaminsky
 * Copyright (C) 2020 Tobias Kaminsky
 * Copyright (C) 2020 Nextcloud GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.owncloud.android.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nextcloud.client.account.User;
import com.nextcloud.client.di.Injectable;
import com.nextcloud.client.jobs.BackgroundJobManager;
import com.owncloud.android.R;
import com.owncloud.android.utils.theme.ViewThemeUtils;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AccountRemovalConfirmationDialog extends DialogFragment implements Injectable {
    private static final String KEY_USER = "USER";

    @Inject BackgroundJobManager backgroundJobManager;
    @Inject ViewThemeUtils viewThemeUtils;
    private User user;

    public static AccountRemovalConfirmationDialog newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_USER, user);

        AccountRemovalConfirmationDialog dialog = new AccountRemovalConfirmationDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            user = arguments.getParcelable(KEY_USER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {

            MaterialButton positiveButton = (MaterialButton) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                viewThemeUtils.material.colorMaterialButtonPrimaryTonal(positiveButton);
            }

            MaterialButton negativeButton = (MaterialButton) alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                viewThemeUtils.material.colorMaterialButtonPrimaryBorderless(negativeButton);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.delete_account)
            .setMessage(getResources().getString(R.string.delete_account_warning, user.getAccountName()))
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton(R.string.common_ok,
                               (dialogInterface, i) -> backgroundJobManager.startAccountRemovalJob(user.getAccountName(),
                                                                                                   false))
            .setNegativeButton(R.string.common_cancel, null);

        viewThemeUtils.dialog.colorMaterialAlertDialogBackground(requireActivity(), builder);

        return  builder.create();
    }
}
