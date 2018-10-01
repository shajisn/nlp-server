package com.inapp.nlp.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.sequencevectors.SequenceVectors;
import org.deeplearning4j.models.sequencevectors.interfaces.SequenceElementFactory;
import org.deeplearning4j.models.sequencevectors.serialization.VocabWordFactory;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NLPSequenceSearcher {

	private static Logger log = LoggerFactory.getLogger(NLPSequenceSearcher.class);

	private static NLPSequenceSearcher instance;

	private InputStream inputStream = null;
	
	private SequenceVectors<VocabWord> vectors = null;

	private NLPSequenceSearcher() throws Exception {
		/*
		 * Load the model and test with some data
		 */
		log.info("Loading the NLP Model ...");
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		inputStream = classloader.getResourceAsStream("seq_vec.model");
		
		log.info("Reading sequence vectors from the NLP Model ...");
		SequenceElementFactory<VocabWord> f1 = new VocabWordFactory();
		vectors = WordVectorSerializer.readSequenceVectors(f1, inputStream);
		if (vectors == null) {
			log.error("NLPSequenceSearcher reading sequence vector from model failed.");
			throw new Exception("NLPSequenceSearcher reading sequence vector from model failed.");
		} else {
			log.info("Model Loading Successful");
			System.out.println("Model Loading Successful");
		}
		
		log.info("NLP Model is loaded into memory ...");
	}

	@Override
	public void finalize() {
		log.info("NLPSequenceSearcher is getting destroyed ...");
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static synchronized NLPSequenceSearcher getInstance() throws Exception {
		if (instance == null) {
			log.info("NLPSequenceSearcher creating instance ...");
			instance = new NLPSequenceSearcher();
		}
		return instance;
	}

	/**
	 * function to check word similarity
	 * 
	 * @param searchWord
	 * @return
	 * @throws Exception
	 */
	public Collection<String> findNearWords(String searchWord) throws Exception {

		try {
			log.info("NLPSequenceSearcher finding sequence vector for word [" + searchWord + "].");
			if(vectors == null) {
				log.error("Loading NLP model is UnSuccesfull !!!");
				return null;
			}
			
			if (searchWord != null && searchWord.trim().length() > 0) {
				searchWord = searchWord.trim().replaceAll(" ", "_");
				log.info("Finding Words near to " + searchWord);
				long stime = Calendar.getInstance().getTimeInMillis();

				// Prints out the closest 100 words
				Collection<String> lst = new ArrayList<String>();
				try {
					lst = vectors.wordsNearest(searchWord, 10);
				} catch (Exception e) {
					log.error("Could not find near by words. Error = " + e.getLocalizedMessage());
					log.error("Error stack", e);
				}
				long etime = Calendar.getInstance().getTimeInMillis();
				log.info("Look up Time is : " + (etime - stime) + " MilliSeconds");
				if (lst != null && lst.size() > 0) {
					log.info("10 Words closest to '" + searchWord + "': " + Arrays.toString(lst.toArray()));
				} else {
					log.info("No Words matched in Corpus !!!!");
				}
				return lst;
			} else {
				log.info("No Words given to search !!!");
				return null;
			}
		} catch (Exception e) {
			log.error("Error in finding near by words. Error = " + e.getLocalizedMessage());
			log.error("Error stack", e);
			throw e;
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Testing NLPSequenceSearcher ....");
		try {
			NLPSequenceSearcher thisIns = NLPSequenceSearcher.getInstance();
			thisIns.findNearWords("trump");
		} catch (Exception e) {
			System.out.println("Error ---------" + e.getMessage());
			e.printStackTrace();
		}
	}

}
