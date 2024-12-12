package campus.data.query.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import campus.data.query.NoSuchAttributeException;

import java.util.function.Predicate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import campus.data.query.Attribute;
import campus.data.query.AttributeOption;
import campus.data.query.Table;
import campus.data.query.TableCreationException;
import campus.data.query.TableFetchException;
import campus.data.query.TableUpdateException;
import campus.data.query.Tuple;

import campus.data.query.csv.io.CSVAdapter;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class CSVTable implements Table {
    private static class CSVTuple implements Tuple {
        private final HashMap<String, ?> attributeValues;

        public CSVTuple(HashMap<String, ?> attributeValues) {
            this.attributeValues = attributeValues;
        }

        @Override
        public Set<String> getAttributeNames() {
            // TODO H3 Implementieren Sie diese Methode
            return attributeValues.keySet().stream().collect(Collectors.toUnmodifiableSet());
        }

        @Override
        public Object getValue(String attributeName) {
            // TODO H3 Implementieren Sie diese Methode
        	if (!getAttributeNames().contains(attributeName)) {
        		throw new NoSuchAttributeException(attributeName); 
        	}
        	Object value = attributeValues.get(attributeName);
        	
            return value;
        }

        @Override
        public Tuple project(List<String> attributeNames) {
            // TODO H3 Implementieren Sie diese Methode
        	HashMap<String, ?> projectedValues = new HashMap<>(attributeValues);
        	for (String name : attributeNames) {
        		if (!attributeValues.keySet().contains(name)) {
        			throw new NoSuchAttributeException(name);
        		}
        	}
        	for (String name : attributeValues.keySet().stream().collect(Collectors.toList())) {
        		if (!attributeNames.contains(name)) {
        			projectedValues.remove(name);
        		}
        	}
            return new CSVTuple(projectedValues);
        }
    }

    private final CSVAdapter adapter;
    private final List<Attribute> attributes;
    private final CSVTableTypeMap typeMap;

    @Override
    public Stream<Tuple> select() {
        // TODO H3 Implementieren Sie diese Methode
    	Stream<Tuple> zeilen;
    	try {
    		return adapter.readRows().map(str -> rowToTuple(str));
        	
    	} catch (IOException e) {
    		throw new TableFetchException("Table file could not be read", e);
    	}

		 
    }

    @Override
    public Tuple insert(Map<String, ?> values) {
        // TODO H3 Implementieren Sie diese Methode
    	Tuple tup = updateTuple(null, values);
    	try {
    		adapter.appendRow(tupleToRow(tup));
    	} catch (IOException e) {
    		throw new TableUpdateException("Table file could not be written", e);
    	}
    	
        return tup;
    }
    
    private long findSmallest(Attribute attr) {
    	long biggest = 0;
    	try {
    		List<Tuple> filtered = select().collect(Collectors.toList());
        	for (Tuple tpl : filtered) {
        		if ((long)tpl.getValue(attr.getName()) > biggest) {
        			biggest = (long) tpl.getValue(attr.getName());
        		}
        	}
        	return biggest++;
    	} catch (Exception e) {
    		throw new TableUpdateException("Table file could not be written", e
    				
    				);
    	}
    	
    }
    
    private boolean isUnique(Attribute attr, Object value) {
    	boolean found = false;
    	try {
    		List<Tuple> searched = select().collect(Collectors.toList());
        	for (Tuple tpl : searched) {
        		if (tpl.getValue(attr.getName()).equals(value)) {
        			found = true;
        		}
        	}
        	return found;
    	} catch (Exception e) {
    		throw new TableUpdateException("Table file could not be written", e);
    	}
    	
    }

    @Override
    public void update(Map<String, ?> newValues, Predicate<? super Tuple> predicate) {
        // TODO H3 Implementieren Sie diese Methode
    	try (Stream<Tuple> tuples = select()) {
    		adapter.writeRows(tuples.map(tpl -> {if (predicate.test(tpl)) {return updateTuple(tpl, newValues);} else {return tpl;}}).map(tpl -> tupleToRow(tpl)));
    	} catch (IOException e) {
    		throw new TableUpdateException("Table file could not be written", e);
    	}
    }

    @Override
    public void delete(Predicate<? super Tuple> predicate) {
        // TODO H3 Implementieren Sie diese Methode
    	try (Stream<String[]> zeilen = select().filter(tpl -> !predicate.test(tpl)).map(tpl -> tupleToRow(tpl))) {
    		adapter.writeRows(zeilen);
    	} catch (IOException e) {
    		throw new TableUpdateException("Table file could not be written", e);
    	}
    	
    	
    }

    private Tuple updateTuple(Tuple tuple, Map<String, ?> newValues) {
        // TODO H3 Implementieren Sie diese Methode
    	HashMap<String, Object> map = new HashMap<>();
    	Iterator<Attribute> it = attributes.iterator();
    	Set<String> attrNames  = attributes.stream().map(attr -> attr.getName()).collect(Collectors.toSet());
    	newValues.keySet().stream().forEach(name -> {if (!attrNames.contains(name)) {throw new NoSuchAttributeException(name);};});
    	while (it.hasNext()) {
    		Attribute next = it.next();
    		if (next.hasOption(AttributeOption.UNIQUE)) {
        		if (isUnique(next, newValues.get(next.getName()))) {
        			throw new TableUpdateException("Duplicate value " + " for unique attribute " + next.getName());
        		}
        	} 
    		if (next.hasOption(AttributeOption.AUTOINCREMENT)) {
    			if (newValues.get(next.getName()) != null) {
    				throw new TableUpdateException("Automatically incrementing attribute " + next.getName() + " may not be set manually");
    			}
    			if (tuple == null) {
    				long smallest = findSmallest(next);
    				if (smallest != 0)
    					smallest++;
             		map.put(next.getName(), smallest);
    			} else {
    				map.put(next.getName(), tuple.getValue(next.getName()));
    			}
    			continue;
           	} else if (newValues.get(next.getName()) == null) {
           		if (next.getDefaultValue() != null) {
           			map.put(next.getName(), next.getDefaultValue());
           			continue;
        		}
           		if (tuple == null) {
           			if (next.hasOption(AttributeOption.NOTNULL)) {
            			throw new TableUpdateException("Attribute " + next.getName() + " may not be NULL");
            		}
            		map.put(next.getName(), next.getDefaultValue());
           		} else {
           			map.put(next.getName(), tuple.getValue(next.getName()));
           		}
           		continue;
    			
        	} 
        	
    		if (tuple == null) {
        		map.put(next.getName(), newValues.get(next.getName()));
        	} else {
        		map.put(next.getName(), newValues.get(next.getName()));
        	}
        	
    		
    	}
    	CSVTuple tup = new CSVTuple(map);
        return tup;
    }

    private String[] tupleToRow(Tuple tuple) {
        String name = attributes.get(0).getName();
        Class<?> type = attributes.get(0).getType();
        Object value = tuple.getValue(name);
        typeMap.writeValue(value, type);
        return attributes.stream()
            .map(attribute -> typeMap.writeValue(
                tuple.getValue(attribute.getName()), attribute.getType()))
            .toArray(String[]::new);
    }

    private Tuple rowToTuple(String[] row) {
        HashMap<String, Object> attributeValues = new HashMap<>();
        for (var i = 0; i < attributes.size(); i++) {
            attributeValues.put(
                attributes.get(i).getName(),
                typeMap.readValue(row[i], attributes.get(i).getType()));
        }
        return new CSVTuple(attributeValues);
    }

    private CSVTable(CSVAdapter adapter, Attribute[] attributes, CSVTableTypeMap typeMap) {
        this.adapter = adapter;
        this.typeMap = typeMap;
        this.attributes = Arrays.asList(attributes);
    }

    static void create(CSVAdapter adapter, Attribute[] attributes, CSVTableTypeMap typeMap) throws IOException {
        findDuplicateAttributeName(attributes).ifPresent(attributeName -> {
            throw new TableCreationException(
                "Duplicate attribute name " + attributeName);
        });

        adapter.setHeaders(attributesToHeaders(attributes, typeMap));
    }

    static CSVTable fromFile(CSVAdapter adapter, CSVTableTypeMap typeMap) throws IOException {
        var descriptors = adapter.getHeaders();
        var attributes = headersToAttributes(descriptors, typeMap);

        findDuplicateAttributeName(attributes).ifPresent(attributeName -> {
            throw new CSVTableFormatException(
                "Duplicate attribute name " + attributeName);
        });

        return new CSVTable(adapter, attributes, typeMap);
    }

    private static String[] attributesToHeaders(Attribute[] attributes, CSVTableTypeMap typeMap) {
        return Arrays.stream(attributes).map(attribute -> {
            var type = attribute.getType();
            var defaultValue = attribute.getDefaultValue();

            var builder = new StringBuilder(attribute.getName())
                .append(":")
                .append(typeMap.nameForType(type));

            if (defaultValue != null) {
                builder
                    .append("[")
                    .append(typeMap.writeValue(defaultValue, type))
                    .append("]");
            }

            if (!attribute.getOptions().isEmpty()) {
                var options = attribute.getOptions().stream()
                    .map(AttributeOption::name)
                    .collect(Collectors.joining(",", "(", ")"));
                builder.append(options);
            }

            return builder.toString();
        }).toArray(String[]::new);
    }

    private static Attribute[] headersToAttributes(String[] headers, CSVTableTypeMap typeMap) {
        var pattern = Pattern.compile("\\A"
            + "(?<name>\\w+)\\s*"
            + ":\\s*(?<type>\\w+)\\s*"
            + "(?:\\[(?<default>.+)])?\\s*"
            + "(?:\\((?<options>\\w+(?:\\s*,\\s*\\w+)*)\\))?"
            + "\\z");
        return Arrays.stream(headers).map(header -> {
            var matcher = pattern.matcher(header.strip());
            if (matcher.matches()) {
                var name = matcher.group("name");
                var typeName = matcher.group("type");
                var type = typeMap.typeForName(typeName);

                var defaultValue = Optional.ofNullable(matcher.group("default"))
                    .map(value -> typeMap.readValue(value, type))
                    .orElse(null);

                var options = Optional.ofNullable(matcher.group("options"))
                    .stream()
                    .flatMap(optionList -> Arrays.stream(optionList.split(",")))
                    .map(option -> AttributeOption.valueOf(option.strip()))
                    .toArray(AttributeOption[]::new);

                return new Attribute(name, type, defaultValue, options);
            } else {
                throw new CSVTableFormatException(
                    String.format("Malformed header: %s", header));
            }
        }).toArray(Attribute[]::new);
    }

    private static Optional<String> findDuplicateAttributeName(Attribute[] attributes) {
        var attributeOccurrences = Arrays.stream(attributes)
            .collect(Collectors.groupingBy(Attribute::getName,
                Collectors.counting()));
        return attributeOccurrences.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .findFirst();
    }
}
