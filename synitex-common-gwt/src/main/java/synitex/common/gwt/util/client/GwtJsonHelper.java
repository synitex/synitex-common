package synitex.common.gwt.util.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GwtJsonHelper {

	private GwtJsonHelper() {
	}

    public static JSONValue toJsonValue(Object value) {
        if(value instanceof JSONValue){
            return (JSONValue) value;
        } if (value instanceof GwtJsonBuilder) {
            return ((GwtJsonBuilder) value).toJson();
        } else if (value instanceof String) {
            return new JSONString((String) value);
        } else if (value instanceof Long) {
            return new JSONString(((Long) value).toString());
        } else if (value instanceof Boolean) {
            return JSONBoolean.getInstance(Boolean.TRUE.equals((Boolean) value));
        } else if (value instanceof Integer) {
            return new JSONString(((Integer) value).toString());
        } else if (value instanceof Double) {
            return new JSONString(((Double) value).toString());
        } else if (value instanceof Date) {
            return new JSONString(String.valueOf(((Date) value).getTime()));
        } else if(value instanceof IGwtJsonDto) {
            IGwtJsonDto gwtDto = (IGwtJsonDto) value;
            return gwtDto.toJsonObject(new GwtJsonBuilder()).toJson();
        } else if (value instanceof Collection) {
            return listToJsonValue((Collection<?>) value);
        } else if(value instanceof JsObject) {
            return new JSONObject((JavaScriptObject) value);
        } else if(value instanceof Map) {
            return mapToJsonValue((Map<?,?>) value);
        } else {
            return null;
        }
    }

    private static JSONValue mapToJsonValue(Map<?, ?> value) {
        if(GwtHelper.isEmpty(value))  {
            return null;
        }
        GwtJsonBuilder builder = new GwtJsonBuilder();
        for(Entry<?,?> en : value.entrySet()) {
            String key = en.getKey().toString();
            builder.append(key, en.getValue());
        }
        return builder.toJson();
    }

    public static JSONValue listToJsonValue(Collection<?> values) {
        JSONArray array = new JSONArray();
        if (GwtHelper.isEmpty(values)) {
            return array;
        }
        int index = 0;
        for (Object value : values) {
            JSONValue jsonVal = toJsonValue(value);
            if (jsonVal != null) {
                array.set(index, jsonVal);
            } else {
                GWT.log("Unsupported list type detected " + value.getClass());
            }
            index++;
        }
        return array;
    }

	public static JSONObject getJsonObject(JSONObject json, String param) {
		JSONValue value = json.get(param);
		if (value == null) {
			return null;
		}
		return value.isObject();
	}

	public static JSONObject getJsonFromString(String str) {
		if (GwtHelper.isEmpty(str)) {
			return null;
		}
        try {
            JSONValue value = JSONParser.parseStrict(str);
            if (value == null) {
                return null;
            }
            return value.isObject();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse json from string: " + str, ex);
        }
	}

	public static JSONObject getJsonFromElement(String id) {
		JSONValue value = getJsonValueFromElement(id);
		if (value == null) {
			return null;
		}
		return value.isObject();
	}

	public static JSONValue getJsonValueFromElement(String id) {
		Element el = DOM.getElementById(id);
		if (el == null) {
			return null;
		}
		String json = el.getInnerHTML();
		if (json == null || "".equals(json)) {
			return null;
		}
		return JSONParser.parseStrict(json);
	}

	public static String getString(JSONObject json, String param) {
		if (json == null || param == null) {
			return null;
		}
		JSONValue value = json.get(param);
		if (value == null || value.isNull() != null) {
			return null;
		}
		JSONString s = value.isString();
		if (s == null) {
			return null;
		}
		return s.stringValue();
	}

	public static boolean getPrimitiveBoolean(JSONObject json, String param) {
		return getPrimitiveBoolean(json, param, false);
	}

	public static boolean getPrimitiveBoolean(JSONObject json, String param, boolean defaultValue) {
		if (json == null || param == null) {
			return defaultValue;
		}
		JSONBoolean value = (JSONBoolean) json.get(param);
		if (value == null) {
			return defaultValue;
		}
		return value.booleanValue();
	}

	public static long getPrimitiveLong(JSONObject json, String param) {
		Long value = getLong(json, param);
		return value == null ? 0L : value.longValue();
	}
	
	public static int getPrimitiveInt(JSONObject json, String param) {
		if (json == null || param == null) {
			return -1;
		}
		JSONNumber value = (JSONNumber) json.get(param);
		if (value == null) {
			return -1;
		}
		double d = value.doubleValue();
		return Double.valueOf(d).intValue();
	}

	public static Integer getInteger(JSONObject json, String param) {
		if (json == null || param == null) {
			return null;
		}

        JSONValue jp = json.get(param);
        if(jp == null || jp.isNull() != null) {
            return null;
        }

		JSONNumber value = jp.isNumber();
		if (value == null) {
			return null;
		}
		double d = value.doubleValue();
		return Double.valueOf(d).intValue();
	}

    public static double getPrimitiveDouble(JSONObject json, String param) {
        Double value = getDouble(json, param);
        return value == null ? 0d : value.doubleValue();
    }

    public static Double getDouble(JSONObject json, String param) {
        if (json == null || param == null) {
            return null;
        }

        JSONValue jp = json.get(param);
        if(jp == null || jp.isNull() != null) {
            return null;
        }

        JSONNumber value = jp.isNumber();
        if (value == null) {
            return null;
        }
        double d = value.doubleValue();
        return Double.valueOf(d);
    }

    public static int getInt(JSONObject json, String param) {
		Integer value = getInteger(json, param);
		return value == null ? 0 : value.intValue();
	}

    public static int getInt(JSONObject json, String param, int defaultValue) {
        Integer value = getInteger(json, param);
        return value == null ? defaultValue : value.intValue();
    }

	public static List<Integer> getIntegers(JSONObject json, String param) {
		return getArray(json, param, new ValueParser<Integer>() {
            @Override
            public Integer parse(JSONValue json) {
                JSONNumber value = (JSONNumber) json;
                if (value == null) {
                    return null;
                }
                double d = value.doubleValue();
                return Double.valueOf(d).intValue();
            }
        });
	}

    public static List<String> getStrings(JSONObject json, String param) {
        return getArray(json, param, new ValueParser<String>() {
            @Override
            public String parse(JSONValue json) {
                JSONString jsonString = json.isString();
                if (jsonString == null) {
                    return null;
                }
                return jsonString.stringValue();
            }
        });
    }

    public static Date getDate(JSONObject json, String param) {
        Long time = getLong(json, param);
        return time == null ? null : new Date(time);
    }

	public static Long getLong(JSONObject json, String param) {
        if (json == null || param == null) {
            return null;
        }
        JSONValue jp = json.get(param);
        if(jp == null || jp.isNull() != null) {
            return null;
        }
        JSONNumber value = jp.isNumber();
        if (value == null) {
            return null;
        }
        double d = value.doubleValue();
        return Double.valueOf(d).longValue();
	}

	public static List<Long> getLongs(JSONObject json, String param) {
		return getArray(json, param, new ValueParser<Long>() {
			@Override
			public Long parse(JSONValue json) {
				JSONString stringValue = json.isString();
				if (stringValue == null) {
					return null;
				}
				String value = stringValue.stringValue();
				if (GwtHelper.isEmpty(value)) {
					return null;
				}
				try {
					return Long.valueOf(value);
				} catch (Exception e) {
					return null;
				}
			}
		});
	}

	public static <RESULT> List<RESULT> getArray(JSONObject json, String param, ValueParser<RESULT> valueParser) {
		List<RESULT> res = new ArrayList<RESULT>();
        if(json == null) {
            return res;
        }
		JSONValue value = json.get(param);
		if (value == null) {
			return res;
		}
		JSONArray array = value.isArray();
		if (array == null || array.size() == 0) {
			return res;
		}
		for (int i = 0; i < array.size(); i++) {
			JSONValue item = array.get(i);
			if (item != null) {
				RESULT r = valueParser.parse(item);
				if (r != null) {
					res.add(r);
				}
			}
		}
		return res;
	}

	public static <RESULT> List<RESULT> getArray(JSONValue json, ValueParser<RESULT> valueParser) {
		if (json == null) {
			return Collections.emptyList();
		}
		JSONArray array = json.isArray();
		if (array == null) {
			return Collections.emptyList();
		}
		int size = array.size();
		if (size == 0) {
			return Collections.emptyList();
		}
		List<RESULT> res = new ArrayList<RESULT>(size);
		for (int i = 0; i < array.size(); i++) {
			JSONValue item = array.get(i);
			if (item != null) {
				RESULT r = valueParser.parse(item);
				if (r != null) {
					res.add(r);
				}
			}
		}
		return res;
	}

	public static interface ValueParser<RESULT> {
		RESULT parse(JSONValue json);
	}

}
