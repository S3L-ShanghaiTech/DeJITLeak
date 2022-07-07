# side-channel-noise
Quantification of side-channel leakage under noisy observations.

This is a collection of octave scripts for computing and visualizing side channel information leakage under noisy observations.

Basic functionality outputs a table of observations and corresponding leaked information with expected information leakage summarized at the end.

More advanced usage produces a visualization of the conditional entropy computation.

Requires an installation of Octave. On Ubuntu:
apt-get install octave

# Input Format

Input is a space or tab separated file. Four sample input files are provided in the data directory.


	# Header: meta data information
	# name: data_name
	# type: matrix
	# rows: number of data points (m) + 2 extra lines for observations labels and probabilities
	# columns: number of observation classes
	 obs_label_1  obs_label_2  ...  obs_label_n
	 p_obs_1      p_obs_2      ...  p_obs_n
	 x_1_1        x_2_1        ...  x_n_1
	 x_1_2        x_2_2        ...  x_n_2
	 .            .                 .
	 .            .                 .
	 .            .                 .
	 x_1_m        x_2_m        ...  x_n_m

# Basic usage (leakage computation)

Simplest use is to call the leakage script on a data file of noisy data observations. Produces a table of leakage given observation and then outputs expected leakage (conditional entropy H(input|observation)).

    octave leakage data/passwordData_length_3_overlap
	   Obs        Leakage
	   0.051602   2.000000
	   0.052309   2.000000
	   0.053016   2.000000
	   .
	   .
	   .
	   0.064330   1.277752
	   0.065037   1.456128
	   0.065744   1.408011
	   .
	   .
	   .	
	   0.120193   2.000000
	   0.120900   2.000000
	   0.121607   2.000000

	Expected Leakage = 1.5013

# Visualization

Inside src directory, from within an Octave session, run detailedPasswordExample to see how the visualization works.


	$ octave
	octave>> detailedPasswordExample

This produces 

1. Plot of the raw data (top left).
2. Vertical distribution plot of the raw data (top right).
3. Distribution overlay for each observable (middle left).
4. Normalized log distributions for each observable (bottom left).
5. Animated conditional probability bar chart (middle right).
6. Leakage vs Observation plot (bottom right). 


![Alt text](visual-leak.png?raw=true "Leakage Visualization.")