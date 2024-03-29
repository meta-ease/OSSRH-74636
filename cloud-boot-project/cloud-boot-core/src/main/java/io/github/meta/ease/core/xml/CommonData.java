/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.meta.ease.core.xml;

import java.util.Iterator;

public interface CommonData {
    public abstract String getName();

    public abstract Object getValue();

    public abstract void setValue(Object paramObject);

    public abstract Object getAttribute(String paramString);

    public abstract void setAttribute(String paramString, Object paramObject);

    public abstract Iterator attributeNames();

    public abstract CommonData addChild(String paramString);

    public abstract CommonData getChild(String paramString);

    public abstract CommonData getChild(int paramInt);

    public abstract int childCount();

    public abstract CommonData getParent();
}
