# Sugar

**Syntactic sugar for Java** - Write expressive, functional-style Java code with less boilerplate.

Sugar is a lightweight utility library that brings the elegance of functional programming to Java. It provides a collection of static helper methods that let you write cleaner, more readable code without the verbosity that Java is known for.

## Why Sugar?

Java's standard library is powerful but verbose. Sugar fixes that:

```java
// Without Sugar
List<String> names = users.stream()
    .filter(u -> u.getAge() > 18)
    .map(User::getName)
    .collect(Collectors.toList());
String first = names.isEmpty() ? null : names.get(0);

// With Sugar
List<String> names = map(filter(users, u -> u.getAge() > 18), User::getName);
String first = first(names);
```

## Installation

### Maven
```xml
<dependency>
    <groupId>com.akalea</groupId>
    <artifactId>sugar</artifactId>
    <version>0.0.15</version>
</dependency>
```

### Import
```java
import static com.akalea.sugar.Collections.*;
import static com.akalea.sugar.Strings.*;
import static com.akalea.sugar.Pojos.*;
import static com.akalea.sugar.Numbers.*;
import static com.akalea.sugar.Files.*;
import static com.akalea.sugar.Functions.*;
import static com.akalea.sugar.Parallel.*;
```

---

## Features at a Glance

| Module | Purpose |
|--------|---------|
| **Collections** | List/Set/Map creation, filtering, mapping, grouping, aggregations |
| **Strings** | String manipulation, case conversion, templates, parsing |
| **Pojos** | Null-safe operations, optional handling, reflection utilities |
| **Numbers** | Clamping, ranges, rounding, interpolation, safe parsing |
| **Files** | Simple file I/O, line reading, resource management |
| **Functions** | Composition, currying, memoization, partial application |
| **Parallel** | Parallel execution, retry logic, timeouts, debounce/throttle |
| **Try/Either/Validation** | Functional error handling without exceptions |

---

## Collections

Create and manipulate collections with minimal code.

### Creating Collections
```java
List<Integer> numbers = list(1, 2, 3, 4, 5);
Set<String> tags = set("java", "kotlin", "scala");
Map<String, Integer> scores = map(kv("alice", 95), kv("bob", 87));
```

### Transformations
```java
// Map and filter
List<String> upperNames = map(names, String::toUpperCase);
List<User> adults = filter(users, u -> u.getAge() >= 18);

// Flat map
List<String> allTags = flatMap(posts, Post::getTags);

// Distinct by property
List<User> uniqueByEmail = distinctBy(users, User::getEmail);
```

### Aggregations
```java
Integer total = sum(prices);
Double average = mean(scores);
Integer highest = max(values);
Map<String, List<Order>> byStatus = groupBy(orders, Order::getStatus);
Map<String, Integer> countByCategory = counts(products, Product::getCategory);
```

### Accessing Elements
```java
User first = first(users);                    // First element or null
User last = last(users);                      // Last element or null
User found = first(filter(users, u -> u.getId() == 42));  // Find by condition
List<User> top5 = take(users, 5);             // First N elements
List<User> rest = drop(users, 5);             // Skip first N elements
```

### Advanced Operations
```java
// Sliding windows
List<List<Integer>> windows = sliding(list(1,2,3,4,5), 3);
// [[1,2,3], [2,3,4], [3,4,5]]

// Running totals with scan
List<Integer> runningSum = scan(list(1,2,3,4), 0, (acc, x) -> acc + x);
// [0, 1, 3, 6, 10]

// Take/drop while condition holds
List<Integer> prefix = takeWhile(list(1,2,3,4,1), x -> x < 4);  // [1,2,3]
List<Integer> suffix = dropWhile(list(1,2,3,4,1), x -> x < 4);  // [4,1]

// Zip two lists together
List<Pair<String, Integer>> pairs = zip(names, ages);

// Interleave lists
List<Integer> mixed = interleave(list(1,3,5), list(2,4,6));  // [1,2,3,4,5,6]
```

### Predicate & Comparator Builders
```java
// Combine predicates
Predicate<User> filter = and(
    u -> u.getAge() >= 18,
    u -> u.isActive(),
    u -> u.getCountry().equals("US")
);

// Build comparators
Comparator<User> byAgeDesc = comparing(User::getAge, true);  // descending
List<User> sorted = sorted(users, byAgeDesc);
```

### Map Utilities
```java
Map<String, Integer> updated = mapValues(scores, v -> v + 10);
Map<String, String> keysMapped = mapKeys(data, String::toUpperCase);
Map<Integer, String> inverted = invert(map);  // Swap keys and values
Map<String, Integer> merged = merge(map1, map2, Integer::sum);
```

---

## Strings

Comprehensive string utilities without Apache Commons.

### Checks
```java
isEmpty(str)      // null or ""
isBlank(str)      // null, "", or whitespace only
isNotEmpty(str)   // opposite of isEmpty
isNotBlank(str)   // opposite of isBlank
```

### Manipulation
```java
truncate("Hello World", 8)          // "Hello..."
truncate("Hello World", 8, ">>")    // "Hello >"
padLeft("42", 5, '0')               // "00042"
padRight("Hi", 5, '.')              // "Hi..."
repeat("ab", 3)                     // "ababab"
reverse("hello")                    // "olleh"
```

### Case Conversion
```java
camelCase("hello_world")    // "helloWorld"
snakeCase("helloWorld")     // "hello_world"
kebabCase("helloWorld")     // "hello-world"
capitalize("hello")         // "Hello"
uncapitalize("Hello")       // "hello"
```

### Parsing & Extraction
```java
substringBefore("hello@world.com", "@")     // "hello"
substringAfter("hello@world.com", "@")      // "world.com"
substringAfterLast("a/b/c.txt", "/")        // "c.txt"
removePrefix("/api/users", "/api")          // "/users"
removeSuffix("file.txt", ".txt")            // "file"
split("a,b,c", ",")                         // ["a", "b", "c"]
```

### Templates
```java
String msg = template("Hello ${name}, you have ${count} messages",
    kv("name", "Alice"),
    kv("count", 5));
// "Hello Alice, you have 5 messages"
```

### Defaults
```java
defaultIfEmpty(str, "default")   // Returns "default" if str is null or ""
defaultIfBlank(str, "default")   // Returns "default" if str is null, "", or whitespace
nullToEmpty(str)                 // Returns "" if str is null
```

---

## Null-Safe Operations (Pojos)

Handle nulls gracefully without endless null checks.

### Safe Value Access
```java
// orElse - return default if null
String name = orElse(user.getName(), "Unknown");

// apply - transform if not null, else null
Integer length = apply(str, String::length);  // null if str is null

// ifPresent - execute only if not null
ifPresent(user, u -> sendEmail(u.getEmail()));

// coalesce - return first non-null value
String value = coalesce(primary, secondary, fallback);
```

### Safe Chaining
```java
// Chain through potentially null objects
String city = chain(user,
    u -> u.getAddress(),
    a -> a.getCity());  // null if any step is null

// safeGet - Optional-based safe access
Optional<String> city = safeGet(user,
    u -> u.getAddress(),
    a -> a.getCity());
```

### Conditional Execution
```java
// when - execute if condition is true
Optional<String> result = when(user.isActive(), () -> "Active User");

// unless - execute if condition is false
unless(list.isEmpty(), () -> process(list));
```

### Equality Checks
```java
equalsAny(status, "PENDING", "PROCESSING", "QUEUED")  // true if matches any
equalsNone(status, "FAILED", "CANCELLED")              // true if matches none
```

---

## Numbers

Numeric utilities for common operations.

### Clamping & Ranges
```java
int clamped = clamp(value, 0, 100);         // Keep value between 0-100
boolean valid = inRange(score, 0, 100);      // Check if in range
List<Integer> nums = range(0, 10, 2);        // [0, 2, 4, 6, 8]
```

### Rounding & Interpolation
```java
double rounded = roundTo(3.14159, 2);                    // 3.14
double interpolated = lerp(0, 100, 0.5);                 // 50.0
double mapped = mapRange(50, 0, 100, 0, 1);              // 0.5
```

### Safe Parsing
```java
int port = parseInt(portStr, 8080);           // Returns 8080 if parsing fails
double rate = parseDouble(rateStr, 0.0);      // Returns 0.0 if parsing fails
```

### Predicates
```java
isEven(4)  // true
isOdd(4)   // false
sign(-5)   // -1
```

---

## Files

Simple file I/O without boilerplate.

### Reading
```java
String content = read("/path/to/file.txt");
List<String> lines = lines("/path/to/file.txt");
```

### Writing
```java
write("/path/to/file.txt", "Hello, World!");
writeLines("/path/to/file.txt", list("line1", "line2"));
append("/path/to/file.txt", "More content");
```

### Resource Management
```java
// Auto-closing resources
String firstLine = using(
    () -> Files.newBufferedReader(path),
    reader -> reader.readLine()
);

// Process lines as stream
long count = withLines("/path/to/file.txt",
    stream -> stream.filter(line -> line.contains("ERROR")).count());
```

### Utilities
```java
exists("/path/to/file")
isFile("/path/to/file")
isDirectory("/path/to/dir")
listFiles("/path/to/dir")
mkdirs("/path/to/new/dir")
delete("/path/to/file")
```

---

## Functions

Function composition and manipulation utilities.

### Composition
```java
Function<String, Integer> length = String::length;
Function<Integer, Boolean> isEven = n -> n % 2 == 0;

// Compose: length then isEven
Function<String, Boolean> hasEvenLength = compose(length, isEven);
hasEvenLength.apply("hello");  // false (5 is odd)
```

### Partial Application & Currying
```java
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

// Partial application - fix first argument
Function<Integer, Integer> add5 = partial(add, 5);
add5.apply(3);  // 8

// Currying - transform to nested functions
Function<Integer, Function<Integer, Integer>> curriedAdd = curry(add);
curriedAdd.apply(5).apply(3);  // 8
```

### Memoization
```java
// Cache expensive computations
Function<Integer, Integer> fibonacci = memoize(n -> {
    if (n <= 1) return n;
    return fibonacci.apply(n-1) + fibonacci.apply(n-2);
});

// Lazy singleton
Supplier<Config> config = memoize(() -> loadConfigFromFile());
```

### Checked Exception Handling
```java
// Convert checked exceptions to unchecked
Function<String, byte[]> readBytes = unchecked(path -> Files.readAllBytes(Paths.get(path)));
```

---

## Parallel Execution

Concurrent operations made simple.

### Parallel Map & ForEach
```java
// Process items in parallel
List<Result> results = pMap(items, item -> expensiveOperation(item));

// Parallel forEach
pEach(users, user -> sendNotification(user));
```

### Retry Logic
```java
// Retry with fixed delay
String result = retry(
    () -> fetchFromApi(),
    3,                          // max attempts
    Duration.ofSeconds(1)       // delay between attempts
);

// Retry with exponential backoff
String result = retryWithBackoff(
    () -> fetchFromApi(),
    5,                          // max attempts
    Duration.ofMillis(100),     // initial delay
    2.0                         // multiplier
);
```

### Timeouts
```java
// Execute with timeout
Optional<String> result = timeout(
    () -> slowOperation(),
    Duration.ofSeconds(5)
);

// With default value
String result = timeout(
    () -> slowOperation(),
    Duration.ofSeconds(5),
    "default"
);
```

### Racing & Awaiting
```java
// Race - return first completed result
String fastest = race(
    () -> fetchFromServer1(),
    () -> fetchFromServer2(),
    () -> fetchFromServer3()
);

// Await all - wait for all to complete
List<String> allResults = awaitAll(
    () -> fetchFromServer1(),
    () -> fetchFromServer2()
);
```

### Debounce & Throttle
```java
// Debounce - only execute after quiet period
Runnable saveDebounced = debounce(
    () -> saveToDatabase(),
    Duration.ofMillis(500)
);

// Throttle - limit execution rate
Runnable logThrottled = throttle(
    () -> logMetrics(),
    Duration.ofSeconds(1)
);
```

### Timing
```java
// Measure execution time
Timed<Result> timed = timed(() -> expensiveOperation());
System.out.println("Took " + timed.getMillis() + "ms");
Result result = timed.getValue();
```

---

## Functional Error Handling

Handle errors without exceptions using monadic types.

### Try - Computation That May Fail
```java
import com.akalea.sugar.internal.Try;

// Wrap potentially failing code
Try<Integer> result = Try.of(() -> Integer.parseInt(userInput));

// Handle success/failure
result
    .onSuccess(n -> System.out.println("Parsed: " + n))
    .onFailure(e -> System.out.println("Invalid: " + e.getMessage()));

// Transform values
Try<String> doubled = result.map(n -> n * 2).map(Object::toString);

// Recover from failure
Integer value = result
    .recover(e -> 0)           // Default value on failure
    .get();

// Chain operations
Try<User> user = Try.of(() -> findUser(id))
    .flatMap(u -> Try.of(() -> validateUser(u)))
    .flatMap(u -> Try.of(() -> enrichUser(u)));
```

### Either - Union Type for Error Handling
```java
import com.akalea.sugar.internal.Either;

// Left = error, Right = success (by convention)
Either<String, User> result = validateUser(input);

// Handle both cases
String message = result.fold(
    error -> "Error: " + error,
    user -> "Welcome, " + user.getName()
);

// Transform the success value
Either<String, String> name = result.map(User::getName);

// Get with default
User user = result.getOrElse(defaultUser);
```

### Validation - Accumulate Multiple Errors
```java
import com.akalea.sugar.internal.Validation;

// Validate multiple fields, collecting all errors
Validation<String, String> nameV = Validation.valid(name)
    .filter(n -> !n.isEmpty(), "Name required");

Validation<String, Integer> ageV = Validation.valid(age)
    .filter(a -> a >= 0, "Age must be positive")
    .filter(a -> a < 150, "Age must be realistic");

// Combine validations
Validation<String, User> userV = nameV.combine(ageV, User::new);

// Get all errors if invalid
if (userV.isInvalid()) {
    List<String> errors = userV.getErrors();  // All validation errors
}
```

---

## Tuples

Lightweight tuple types for grouping values.

```java
// Pair - two values
Pair<String, Integer> pair = pair("Alice", 30);
String name = pair.getFirst();
Integer age = pair.getSecond();

// Tuple3 - three values
Tuple3<String, Integer, Boolean> t3 = tuple("Alice", 30, true);

// Tuple4 - four values
Tuple4<String, Integer, Boolean, String> t4 = tuple("Alice", 30, true, "US");

// Map operations
Pair<String, Integer> mapped = pair.mapFirst(String::toUpperCase);
```

---

## Lazy Evaluation

Defer expensive computations until needed.

```java
import com.akalea.sugar.internal.Lazy;

// Create a lazy value
Lazy<Config> config = Lazy.of(() -> loadExpensiveConfig());

// Value is computed on first access
Config c = config.get();  // Computed here
Config c2 = config.get(); // Cached, not recomputed

// Check if evaluated
config.isEvaluated();  // true after first get()

// Transform lazily (computation deferred)
Lazy<String> configName = config.map(Config::getName);

// Safe access without triggering evaluation
String name = config.getOrElse("default");  // Returns default if not yet evaluated

// Convert to Try for error handling
Try<Config> tryConfig = config.toTry();
```

---

## Range

Represent and work with numeric ranges.

```java
import com.akalea.sugar.internal.Range;

// Create ranges
Range<Integer> closed = Range.closed(1, 10);    // [1, 10]
Range<Integer> open = Range.open(1, 10);        // (1, 10)
Range<Integer> halfOpen = Range.closedOpen(1, 10);  // [1, 10)

// Unbounded ranges
Range<Integer> atLeast = Range.atLeast(5);      // [5, +∞)
Range<Integer> lessThan = Range.lessThan(10);   // (-∞, 10)

// Check containment
closed.contains(5);      // true
closed.contains(11);     // false
closed.containsAll(list(1, 5, 10));  // true

// Range operations
Range<Integer> r1 = Range.closed(1, 10);
Range<Integer> r2 = Range.closed(5, 15);
r1.overlaps(r2);                    // true
r1.encloses(Range.closed(3, 7));    // true
r1.intersection(r2);                // [5, 10]
r1.span(r2);                        // [1, 15]

// Convert to list
List<Integer> nums = Range.toList(Range.closed(1, 5));  // [1, 2, 3, 4, 5]
List<Integer> evens = Range.toList(Range.closed(0, 10), 2);  // [0, 2, 4, 6, 8, 10]
```

---

## Pattern Matching

Expressive conditional logic with the Match utility.

```java
import com.akalea.sugar.Match;

// Match exact values
String result = Match.of(statusCode)
    .when(200, () -> "OK")
    .when(404, () -> "Not Found")
    .when(500, () -> "Server Error")
    .otherwise(() -> "Unknown");

// Match with predicates
String size = Match.of(value)
    .when(x -> x > 100, () -> "Large")
    .when(x -> x > 10, () -> "Medium")
    .when(x -> x > 0, () -> "Small")
    .otherwise(() -> "Zero or negative");

// Match null values
String display = Match.of(user)
    .whenNull(() -> "Anonymous")
    .when(u -> u.isAdmin(), u -> "Admin: " + u.getName())
    .otherwise(u -> u.getName());

// Match by type
String desc = Match.of(value)
    .whenType(String.class, s -> "String: " + s)
    .whenType(Integer.class, i -> "Integer: " + i)
    .whenType(List.class, l -> "List of " + l.size())
    .otherwise(() -> "Unknown type");

// Match multiple values
String category = Match.of(code)
    .whenAny(() -> "Success", 200, 201, 204)
    .whenAny(() -> "Client Error", 400, 401, 403, 404)
    .otherwise(() -> "Other");

// Match ranges
String grade = Match.of(score)
    .whenInRange(90, 100, () -> "A")
    .whenInRange(80, 89, () -> "B")
    .whenInRange(70, 79, () -> "C")
    .otherwise(() -> "F");

// Throw on no match
String required = Match.of(value)
    .when(1, () -> "One")
    .otherwiseThrow(() -> new IllegalArgumentException("Invalid value"));
```

---

## Dates

Comprehensive date and time utilities.

```java
import static com.akalea.sugar.Dates.*;

// Current date/time
LocalDateTime now = now();
LocalDate today = today();

// Parsing with defaults
LocalDate date = parseDate("2024-01-15", "yyyy-MM-dd", LocalDate.now());
LocalDateTime dt = parseIsoDateTime("2024-01-15T10:30:00", null);

// Formatting
String formatted = formatDate(today, "dd/MM/yyyy");  // "15/01/2024"

// Duration calculations
long days = daysBetween(startDate, endDate);
long months = monthsBetween(startDate, endDate);
long hours = hoursBetween(startTime, endTime);

// Date arithmetic
LocalDate nextWeek = addDays(today, 7);
LocalDate nextMonth = addMonths(today, 1);
LocalDateTime later = addHours(now, 3);

// Boundaries
LocalDateTime dayStart = startOfDay(today);   // 00:00:00
LocalDateTime dayEnd = endOfDay(today);       // 23:59:59.999
LocalDate monthStart = startOfMonth(today);
LocalDate monthEnd = endOfMonth(today);
LocalDate yearStart = startOfYear(today);
LocalDate weekStart = startOfWeek(today);     // Monday

// Predicates
isToday(date);      // true if date is today
isYesterday(date);  // true if date was yesterday
isTomorrow(date);   // true if date is tomorrow
isWeekend(date);    // true if Saturday or Sunday
isWeekday(date);    // true if Monday-Friday
isFuture(date);     // true if after today
isPast(date);       // true if before today
isBetween(date, start, end);  // inclusive check
isLeapYear(date);   // true if leap year

// Human-readable relative time
humanize(Duration.ofHours(3));      // "3 hours ago"
humanize(Duration.ofDays(2));       // "2 days ago"
humanize(LocalDate.now().minusDays(1));  // "yesterday"

// Utilities
int days = daysInMonth(date);       // 28, 29, 30, or 31
int quarter = quarter(date);        // 1, 2, 3, or 4
DayOfWeek dow = dayOfWeek(date);    // MONDAY, etc.

// Conversions
long millis = toEpochMillis(dateTime);
LocalDateTime dt = fromEpochMillis(millis);
```

---

## Additional Collections Features

### Statistics
```java
// Statistical operations
Double med = median(list(1, 2, 3, 4, 5));       // 3.0
Double var = variance(list(1, 2, 3, 4, 5));     // 2.0
Double std = stdDev(list(1, 2, 3, 4, 5));       // ~1.41

// Frequency counting
Map<String, Long> counts = frequencies(list("a", "b", "a", "c", "a"));
// {a=3, b=1, c=1}
```

### Partitioning
```java
// Split by predicate
Pair<List<Integer>, List<Integer>> parts = partition(numbers, n -> n > 0);
List<Integer> positive = parts.getFirst();
List<Integer> nonPositive = parts.getSecond();

// Split at index
Pair<List<T>, List<T>> halves = splitAt(list, 5);

// Split at first non-matching
Pair<List<Integer>, List<Integer>> span = span(list, n -> n < 10);
```

### Set Operations
```java
Set<T> combined = union(set1, set2);
Set<T> symDiff = symmetricDifference(set1, set2);  // In either but not both
```

### Collection Predicates
```java
containsAny(collection, list("a", "b"));   // true if any element present
containsNone(collection, list("x", "y"));  // true if no elements present
containsAll(collection, list("a", "b"));   // true if all elements present
```

---

## Additional Strings Features

### Text Processing
```java
List<String> lineList = lines("line1\nline2\nline3");  // Split by newlines
List<String> wordList = words("hello world");          // Split by whitespace

String indented = indent("hello\nworld", 4);   // Add 4 spaces to each line
String dedented = dedent("    hello\n    world");  // Remove common indent

String wrapped = wrap("Long text that needs wrapping", 20);  // Word wrap
```

### Slug and Masking
```java
String slug = slugify("Hello World!");  // "hello-world"
String masked = mask("1234567890", 2, 2);  // "12******90"
String masked2 = mask("secret", 1, 1, '#');  // "s####t"
```

### Character Checks
```java
isNumeric("123");        // true
isAlpha("abc");          // true
isAlphanumeric("abc123"); // true
isLowerCase("hello");    // true
isUpperCase("HELLO");    // true
```

### Additional String Utils
```java
String centered = center("hi", 10, '-');  // "----hi----"
String title = titleCase("hello world");  // "Hello World"
String inits = initials("John Doe");      // "JD"
String common = commonPrefix(list("prefix_a", "prefix_b"));  // "prefix_"
String abbrev = abbreviate("Long text here", 10);  // "Long..."
```

---

## Additional Numbers Features

### Math Utilities
```java
long g = gcd(48, 18);      // 6 (greatest common divisor)
long l = lcm(4, 6);        // 12 (least common multiple)
boolean prime = isPrime(17);  // true

long fact = factorial(5);  // 120
long fib = fibonacci(10);  // 55
```

### Comparisons
```java
boolean close = closeTo(0.1 + 0.2, 0.3, 0.0001);  // true (floating point safe)
```

### Percentages
```java
double pct = percentage(25, 100);  // 25.0
double val = percentOf(20, 150);   // 30.0 (20% of 150)
```

---

## Additional Files Features

### File Operations
```java
copy("/source/file.txt", "/dest/file.txt");
move("/old/path.txt", "/new/path.txt");
deleteRecursively("/dir/to/delete");  // Deletes directory and all contents
```

### Path Utilities
```java
String ext = extension("/path/to/file.txt");   // "txt"
String base = baseName("/path/to/file.txt");   // "file"
long bytes = size("/path/to/file.txt");
```

### Additional Operations
```java
touch("/path/to/file.txt");  // Create or update modification time
List<Path> allFiles = listFilesRecursively("/dir");
byte[] data = readBytes("/path/to/binary.dat");
writeBytes("/path/to/output.dat", data);
boolean empty = isEmpty("/path/to/check");  // true if file is 0 bytes or dir is empty
```

---

## Real-World Examples

### Processing a CSV File
```java
List<User> users = map(
    drop(lines("/data/users.csv"), 1),  // Skip header
    line -> {
        List<String> parts = split(line, ",");
        return new User(
            parts.get(0),
            parseInt(parts.get(1), 0),
            parts.get(2)
        );
    }
);

Map<String, List<User>> byCountry = groupBy(users, User::getCountry);
```

### API Request with Retry and Timeout
```java
Optional<Response> response = timeout(
    () -> retry(
        () -> httpClient.get(url),
        3,
        Duration.ofSeconds(1)
    ),
    Duration.ofSeconds(10)
);

String body = response
    .map(Response::getBody)
    .orElse("Request failed");
```

### Data Transformation Pipeline
```java
List<Report> reports = map(
    filter(
        distinctBy(orders, Order::getCustomerId),
        o -> o.getTotal() > 100
    ),
    o -> new Report(
        o.getCustomerId(),
        orElse(o.getCustomerName(), "Unknown"),
        roundTo(o.getTotal(), 2)
    )
);
```

### Batch Processing with Progress
```java
List<List<Item>> batches = partition(items, 100);
pEach(batches, batch -> {
    retry(() -> processBatch(batch), 3, Duration.ofSeconds(5));
});
```

---

## Requirements

- Java 9+
- No external dependencies

## License

Apache License 2.0

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.
