/*
 * Copyright © 2024 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TimeDuration implements Token {
    private final String rawValue;
    private final long nanoseconds;

    public TimeDuration(String value) {
        this.rawValue = value;
        this.nanoseconds = parseToNanoseconds(value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(rawValue);
    }

    @Override
    public String value() {
        return rawValue;
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    public long getNanoseconds() {
        return nanoseconds;
    }

    private long parseToNanoseconds(String value) {
        String numPart = value.replaceAll("[^0-9.]", "");
        String unitPart = value.replaceAll("[0-9.]", "").toLowerCase();
        double number = Double.parseDouble(numPart);

        switch (unitPart) {
            case "ns": return (long) number;
            case "us": return (long) (number * 1000);
            case "ms": return (long) (number * 1000 * 1000);
            case "s": return (long) (number * 1000 * 1000 * 1000);
            case "m": return (long) (number * 60 * 1000 * 1000 * 1000);
            case "h": return (long) (number * 3600 * 1000 * 1000 * 1000);
            case "d": return (long) (number * 24 * 3600 * 1000 * 1000 * 1000);
            default: throw new IllegalArgumentException("Invalid time unit: " + unitPart);
        }
    }
}