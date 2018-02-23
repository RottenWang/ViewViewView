package com.drwang.views.dagger;

import javax.inject.Inject;

/**
 * Created by wang on 2018/2/23.
 */

public class Factory {
    Product product;

    @Inject
    public Factory(Product product) {
        this.product = product;
    }

    public void toast() {
        product.toast();
    }
}
