/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.util;

import io.netty.util.AttributeKey;

/**
 * @author wangjiayou 2019/7/3
 * @version ORAS v1.0
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
