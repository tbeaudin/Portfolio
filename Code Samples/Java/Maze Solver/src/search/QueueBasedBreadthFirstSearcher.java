package search;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class StateWithPredecessor<T> {
	private T state;
	private StateWithPredecessor<T> predecessor;
	StateWithPredecessor(StateWithPredecessor<T> predecessor, T state) {
		this.state = state;
		this.predecessor = predecessor;
	}

	public StateWithPredecessor<T> getPredecessor() {
		return predecessor;
	}

	public T getState() {
		return state;
	}
}

/**
 * An implementation of a Searcher that performs an iterative search,
 * storing the list of next states in a Queue. This results in a
 * breadth-first search.
 * 
 */
public class QueueBasedBreadthFirstSearcher<T> extends Searcher<T> {

	public QueueBasedBreadthFirstSearcher(SearchProblem<T> searchProblem) {
		super(searchProblem);
	}

	@Override
	public List<T> solve() {
		 Queue<StateWithPredecessor<T>> nextStatesQueue = new LinkedList<StateWithPredecessor<T>>(); // Stores the next states to visit
		 LinkedList<StateWithPredecessor<T>> BFSPathTraversal = new LinkedList<StateWithPredecessor<T>>(); // Stores the BFS Traversal of the states
		 StateWithPredecessor<T> curState; 
		 List<T> successorList;
		 nextStatesQueue.add(new StateWithPredecessor<T>(null, searchProblem.getInitialState()));

		 while (nextStatesQueue.isEmpty() != true) {
			curState = nextStatesQueue.remove();
			BFSPathTraversal.add(curState);
			visitedStates.add(curState.getState());
			if (searchProblem.isGoalState(curState.getState()) != true) {
				successorList = searchProblem.getSuccessors(curState.getState());
				for (int i = 0; i < successorList.size(); i++) {
					if (visitedStates.contains(successorList.get(i)) == false) {
						nextStatesQueue.add(new StateWithPredecessor<T>(curState, successorList.get(i)));
					}
				}
				successorList.clear();
			}
			else {
				break;
			}
		 }
		 List<T> path = new LinkedList<T>();
		 curState = BFSPathTraversal.getLast();
		 while(curState != null) {
			 path.add(curState.getState());
			 curState = curState.getPredecessor();
		 }
		 path = reverseList(path);
         return path;
	}

	private List<T> reverseList(List<T> list) {
		List<T> reversedList = new LinkedList<T>();
		for (int i = list.size()-1; i >= 0; i--) {
			reversedList.add(list.get(i));
		}
		return reversedList;
	}
}


