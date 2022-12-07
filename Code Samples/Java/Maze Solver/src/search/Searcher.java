package search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An abstraction over the idea of a search.
 *
 * @param <T>
 */
public abstract class Searcher<T> {
	protected final SearchProblem<T> searchProblem;
	protected final List<T> visitedStates;
	protected List<T> solution;

	/**
	 * Instantiates a searcher.
	 * 
	 * @param searchProblem
	 *            the search problem for which this searcher will find and
	 *            validate solutions
	 */
	public Searcher(SearchProblem<T> searchProblem) {
		this.searchProblem = searchProblem;
		this.visitedStates = new ArrayList<T>();
	}

	/**
	 * Finds and return a solution to the problem, consisting of a list of
	 * states.
	 * 
	 * The list should start with the initial state of the underlying problem.
	 * Then, it should have one or more additional states. Each state should be
	 * a successor of its predecessor. The last state should be a goal state of
	 * the underlying problem.
	 * 
	 * If there is no solution, then this method should return an empty list.
	 * 
	 * @return a solution to the problem (or an empty list)
	 */
	public abstract List<T> solve();

	/**
	 * Checks that a solution is valid.
	 * 
	 * A valid solution consists of a list of states. The list should start with
	 * the initial state of the underlying problem. Then, it should have one or
	 * more additional states. Each state should be a successor of its
	 * predecessor. The last state should be a goal state of the underlying
	 * problem.
	 * 
	 * @param solution
	 * @return true iff this solution is a valid solution
	 * @throws NullPointerException
	 *             if solution is null
	 */
	public final boolean isValid(List<T> solution) throws NullPointerException {
		if (solution == null) {
			throw new NullPointerException("Solution list is empty!!!");
		}

		if (solution.size() < 2) { // 
			return false;
		}

		Iterator<T> solutionIterator = solution.iterator();
		T predecessor;
		List<T> validStateList;
		T currState = solutionIterator.next();

		if (!currState.equals(searchProblem.getInitialState())) {  // Check if first state in solution list is initial state
			return false;
		}

		while (solutionIterator.hasNext()) {
			predecessor = currState;
			currState = solutionIterator.next();
			validStateList = searchProblem.getSuccessors(predecessor);
			if (!validStateList.contains(currState)) {
				return false;
			}
		}

		if (searchProblem.isGoalState(currState) == false) { // Check if last state in solution list is goal state
			return false;
		}

		return true;
	}
}
