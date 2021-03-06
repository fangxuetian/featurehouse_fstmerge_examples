package net.sf.jabref.bst;

import java.util.Stack;

import net.sf.jabref.bst.VM.BstEntry;
import net.sf.jabref.bst.VM.BstFunction;




public class ChangeCaseFunction implements BstFunction {

	VM vm;

	public ChangeCaseFunction(VM vm) {
		this.vm = vm;
	}

	public void execute(BstEntry context) {
		Stack<Object> stack = vm.getStack();

		if (stack.size() < 2) {
			throw new VMException("Not enough operands on stack for operation change.case$");
		}
		Object o1 = stack.pop();
		Object o2 = stack.pop();

		if (!(o1 instanceof String && ((String) o1).length() == 1)) {
			throw new VMException("A format string of length 1 is needed for change.case$");
		}

		if (!(o2 instanceof String)) {
			throw new VMException("A string is needed as second parameter for change.case$");
		}

		char format = (((String) o1).toLowerCase().charAt(0));
		String s = (String) o2;

		stack.push(BibtexCaseChanger.changeCase(s, format, vm));
	}

}
