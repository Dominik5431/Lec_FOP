package classification.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListDataset<S extends Sample<?>> implements Dataset<S> {

	
	List<S> samples;
	
	public ListDataset() {
		samples = new ArrayList<S>(); //Welcher generische Typparameter wird hier genommen, wenn man nichts hinschreibt?
	}
	
	@Override
	public Iterator<S> iterator() {
		// TODO Auto-generated method stub
		return samples.iterator();
	}

	@Override
	public void add(S sample) {
		// TODO Auto-generated method stub
		samples.add(sample);	//Fügt Element am Ende der Liste ein
	}

	@Override
	public void shuffle() {
		// TODO Auto-generated method stub
		Collections.shuffle(samples);
	}
	
	

}
