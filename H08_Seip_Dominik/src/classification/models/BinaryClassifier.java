package classification.models;

import java.util.List;

import classification.data.BinaryLabel;
import classification.data.Dataset;
import classification.data.Label;
import classification.data.Sample;
import classification.data.SupervisedSample;

/**
 * Dieses Interface definiert die grundlegenden Operationen eines allgemeinen
 * binären Klassifizierers.
 *
 * @author Kim Berninger
 * @version 1.1.0
 *
 * @param <S> der Subtyp von {@link Sample}, dem die Elemente, mit denen dieser
 *            Klassifizierer trainiert werden soll, entsprechen sollen
 */
public interface BinaryClassifier<S extends Sample<? super S>> {
    <L extends Label> List<Double> fit(Dataset<? extends SupervisedSample<? extends S, L>> data, int epochs);

    <L extends Label> double evaluate(Dataset<? extends SupervisedSample<? extends S, L>> data);

    <L extends Label, T extends S> List<BinaryLabel> predict(Dataset<? extends Sample<? super T>> data);
   
    
}


