

import java.util.ArrayList;
class Transition {
	public State from;
	public String operator;
	public State to;
	public Transition(State from, String operator, State to) {
		super();
		this.from = from;
		this.operator = operator;
		this.to = to;
	}
	@Override
	public String toString() {
		return "[from=" + from + ", operator=" + operator + ", to=" + to + "] \n";
	}
	public State getFrom() {
		return from;
	}
	public void setFrom(State from) {
		this.from = from;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public State getTo() {
		return to;
	}
	public void setTo(State to) {
		this.to = to;
	}

	
}
class State {
    private String index;
    
    public State(String index) {
        this.index=index;
    }
  
    @Override
	public String toString() {
		return "State [index=" + index + "]";
	}

	public String getIndex() {
        return this.index;
    }
   
}
class Main  {
	public static void main(String[] args) {
		
		System.out.println("0,0,1;1,2,1;2,0,3;3,3,3#1,3");
		DFA dfa = new DFA("0,0,1;1,2,1;2,0,3;3,3,3#1,3");
		
		System.out.println(dfa.run("11"));
		System.out.println(dfa.run("01010100"));
		System.out.println(dfa.run("100010010"));
		System.out.println(dfa.run("101"));
		System.out.println(dfa.run("0010"));
		
		System.out.println("-------------------------");
		
		System.out.println("0,3,1;1,2,1;2,2,1;3,3,3#2");
		DFA dfa2 = new DFA("0,3,1;1,2,1;2,2,1;3,3,3#2");
		
		System.out.println(dfa2.run("010"));
		System.out.println(dfa2.run("10101010"));
		System.out.println(dfa2.run("10010"));
		System.out.println(dfa2.run("100010011"));
		System.out.println(dfa2.run("010010"));
	}
}
public class DFA {
	ArrayList<Transition> transitions;
	ArrayList<State> states;
	ArrayList<State> acceptStates;

	public DFA(String input) {
		transitions = new ArrayList<Transition>();
		states = new ArrayList<State>();
		acceptStates = new ArrayList<State>();

		String[] inputs = input.split("#");
		String[] statesInput = inputs[0].split(";");
		for (String state : statesInput) {
			String[] stateIn = state.split(",");
			if (!inArray(states, stateIn[0]))
				states.add(new State(stateIn[0]));
		}
		String[] acceptString = inputs[1].split(",");
		for (String a : acceptString) {
			acceptStates.add(getState(states, a));
		}

		for (String state : statesInput) {
			String[] stateIn = state.split(",");
			State from = getState(states, stateIn[0]);
			State to0 = getState(states, stateIn[1]);
			State to1 = getState(states, stateIn[2]);
			Transition t0 = new Transition(from, "0", to0);
			Transition t1 = new Transition(from, "1", to1);
			transitions.add(t0);
			transitions.add(t1);
		}
	}

	public boolean run(String target) {
		ArrayList<State> chars = new ArrayList<State>();
		for (char c : target.toCharArray()) {
			chars.add(new State(Character.toString(c)));
		}
		int size = chars.size();
		State current = null;
		State next;
		for (int i = 0; i < size; i++) {
			if (i == 0)
				current = states.get(0);
			next = nextState(states, current, chars.get(i).getIndex());
			if (next == null)
				return false;
			else {
				State temp = getState(states, current.getIndex());
				current = getState(states, next.getIndex());
				next = nextState(states, temp, chars.get(i).getIndex());
			}
			if (i == size - 1) {
				if (inArray(acceptStates, current.getIndex())) {
					return true;
				} else
					return false;
			}

		}

		return false;
	}

	public State nextState(ArrayList<State> array, State currentState, String op) {
		State from = currentState;
		String operator = op;
		State next = null;

		for (Transition t : transitions) {
			if (t.getFrom().getIndex().equals(from.getIndex()) && t.getOperator().equals(operator)) {
				next = t.getTo();
				break;
			}
		}
		return next;
	}

	public boolean inArray(ArrayList<State> array, String value) {
		boolean found = false;
		for (State s : array) {
			if (s.getIndex() == value) {
				found = true;
			}
		}
		return found;
	}

	public State getState(ArrayList<State> array, String value) {
		for (State s : array) {
			if (s.getIndex().equals(value)) {
				return s;
			}
		}
		return null;
	}
}