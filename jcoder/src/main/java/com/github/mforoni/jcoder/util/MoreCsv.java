package com.github.mforoni.jcoder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import com.github.mforoni.jbasic.JMaps;
import com.github.mforoni.jbasic.JStrings;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.JClass;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jcoder.Names;
import com.google.common.annotations.Beta;
import com.google.common.base.Function;

/**
 * Provides static methods to read data from CSV files into Java Collection objects. It also
 * provided API for retrieving {@link JHeader} objects that can be used for automatically generate
 * JavaBean or immutable java class from CSV files using {@link JClass} functionalities.
 * 
 * @author Foroni Marco
 * @see JHeader
 * @see JClass
 */
@Beta
public final class MoreCsv {
  private MoreCsv() {
    throw new AssertionError();
  }

  public static JHeader inferHeader(final String resource)
      throws FileNotFoundException, IOException {
    return inferHeader(new CsvReader.Builder(resource).build());
  }

  public static JHeader inferHeader(final File csv) throws FileNotFoundException, IOException {
    return inferHeader(new CsvReader(csv));
  }

  public static JHeader inferHeader(final CsvReader csvReader)
      throws FileNotFoundException, IOException {
    final Parser.Strings parser = new Parser.Strings(csvReader.getCsv().getName());
    try (final FileInputStream fis = new FileInputStream(csvReader.getCsv()); //
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
      String line;
      int row = 1;
      while ((line = br.readLine()) != null && row < csvReader.getRowLimit()) {
        final String[] values = JStrings.splitEscapingQuoted(line, csvReader.getSeparator());
        if (csvReader.isHeader() && row == 1) {
          parser.header(values);
        } else {
          parser.values(values, row);
        }
        row++;
      }
    }
    return parser.buildHeader();
  }

  public static List<CSVRecord> parse(final String resource, final String[] header)
      throws IOException {
    return parse(JFiles.fromResource(resource), header);
  }

  public static List<CSVRecord> parse(final File csv, final String[] header) throws IOException {
    final CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(header).withSkipHeaderRecord();
    try (final FileReader fileReader = new FileReader(csv); //
        final CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);) {
      return csvFileParser.getRecords();
    }
  }

  @Nonnull
  public static <T> List<T> parse(@Nonnull final String resource, @Nonnull final Class<T> classType,
      @Nonnull final CellProcessor[] cellProcessors) throws IOException {
    return parse(JFiles.fromResource(resource), classType, cellProcessors);
  }

  @Nonnull
  public static <T> List<T> parse(@Nonnull final File csv, @Nonnull final Class<T> classType,
      @Nonnull final CellProcessor[] cellProcessors) throws IOException {
    final List<T> list = new ArrayList<>();
    try (final FileReader reader = new FileReader(csv);
        final ICsvBeanReader beanReader =
            new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
      // the header elements are used to map the values to the bean (names must match)
      final String[] header = beanReader.getHeader(true);
      final String[] nameMapping = new String[header.length];
      for (int i = 0; i < header.length; i++) {
        nameMapping[i] = Names.ofField(header[i]);
      }
      T obj;
      while ((obj = beanReader.read(classType, nameMapping, cellProcessors)) != null) {
        list.add(obj);
      }
    }
    return list;
  }

  /**
   * Returns a {@link Map} of generic type objects from a CSV file providing a {@link CellProcessor}
   * and a type-key conversion {@link Function}. If the generic type is automatically generated the
   * {@code CellProcessor} is typically a {@code public static final} member of the generic type.
   * <p>
   * Note: the uniqueness of the key-value mappings is ensured.
   * 
   * @param csvResourceName the CSV file resource name
   * @param classType the generic type
   * @param cellProcessors
   * @param function the generic type to key {@code Function}
   * @return a {@code Map}
   * @throws IOException
   * @see JFiles#fromResource(String)
   * @see CellProcessor
   * @see Function
   * @see JMaps#putEnsuringNoCollision(Map, Object, Object)
   */
  @Nonnull
  public static <K, T> Map<K, T> toMapEnsuringUniqueness(final String csvResourceName,
      final Class<T> classType, final CellProcessor[] cellProcessors, final Function<T, K> function)
      throws IOException {
    return _toMap(JFiles.fromResource(csvResourceName), classType, cellProcessors, function, true);
  }

  /**
   * Returns a {@link Map} of generic type objects from a CSV file providing a {@link CellProcessor}
   * and a type-key conversion {@link Function}. If the generic type is automatically generated the
   * {@code CellProcessor} is typically a {@code public static final} member of the generic type.
   * <p>
   * Note: the uniqueness of the key-value mappings is ensured.
   * 
   * @param csv
   * @param classType the generic type
   * @param cellProcessors
   * @param function the generic type to key {@code Function}
   * @return a {@code Map}
   * @throws IOException
   * @see CellProcessor
   * @see Function
   * @see JMaps#putEnsuringNoCollision(Map, Object, Object)
   */
  @Nonnull
  public static <K, T> Map<K, T> toMapEnsuringUniqueness(final File csv, final Class<T> classType,
      final CellProcessor[] cellProcessors, final Function<T, K> function) throws IOException {
    return _toMap(csv, classType, cellProcessors, function, true);
  }

  /**
   * Returns a {@link Map} of generic type objects from a CSV file providing a {@link CellProcessor}
   * and a type-key conversion {@link Function}. If the generic type is automatically generated the
   * {@code CellProcessor} is typically a {@code public static final} member of the generic type.
   * 
   * @param csv
   * @param classType the generic type
   * @param cellProcessors
   * @param function the generic type to key {@code Function}
   * @return a {@code Map}
   * @throws IOException
   * @see CellProcessor
   * @see Function
   */
  @Nonnull
  public static <K, T> Map<K, T> toMap(final File csv, final Class<T> classType,
      final CellProcessor[] cellProcessors, final Function<T, K> function) throws IOException {
    return _toMap(csv, classType, cellProcessors, function, false);
  }

  private static <K, T> Map<K, T> _toMap(final File csv, final Class<T> classType,
      final CellProcessor[] cellProcessors, final Function<T, K> function,
      final boolean ensureUniqueness) throws IOException {
    final Map<K, T> map = new HashMap<>();
    try (final FileReader reader = new FileReader(csv);
        final ICsvBeanReader csvBeanReader =
            new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
      // the header elements are used to map the values to the bean (names must match)
      final String[] header = csvBeanReader.getHeader(true);
      T obj;
      while ((obj = csvBeanReader.read(classType, header, cellProcessors)) != null) {
        final K key = function.apply(obj);
        if (ensureUniqueness) {
          JMaps.putEnsuringNoCollision(map, key, obj);
        } else {
          map.put(key, obj);
        }
      }
    }
    return map;
  }
}
