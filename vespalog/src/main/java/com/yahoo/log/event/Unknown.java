// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.log.event;

/**
 *
 * @author <a href="mailto:borud@yahoo-inc.com">Bjorn Borud </a>
 */
public class Unknown extends Event {
    public Unknown() {
    }

    private String name;

    public Unknown setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
