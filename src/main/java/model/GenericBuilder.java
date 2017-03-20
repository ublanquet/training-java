package model;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

//http://stackoverflow.com/a/31754787

/**
 * Generic static builder class, can be used to build any mutable obj.
 * @param <T> classtype, not necessary
 */
public class GenericBuilder<T> {

    private final Supplier<T> instantiator;

    private List<Consumer<T>> instanceModifiers = new ArrayList<>();

    /**
     * set instantiator.
     * @param instantiator instanciator
     */
    public GenericBuilder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    /**
     * reutrn a generic builder of the right ype.
     * @param instantiator target class instanciator
     * @param <T> target class
     * @return typed generic builder
     */
    public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
        return new GenericBuilder<T>(instantiator);
    }

    /**
     * set any param of the class.
     * @param consumer setter
     * @param value value
     * @param <U> target class
     * @return this
     */
    public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> c = instance -> consumer.accept(instance, value);
        instanceModifiers.add(c);
        return this;
    }

    /**
     * final builder method.
     * @return created object
     */
    public T build() {
        T value = instantiator.get();
        instanceModifiers.forEach(modifier -> modifier.accept(value));
        instanceModifiers.clear();
        return value;
    }
}

/*Person value = GenericBuilder.of(Person::new)
            .with(Person::setName, "Otto").with(Person::setAge, 5).build();*/