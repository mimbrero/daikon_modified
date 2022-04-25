# DAIKON
This is a modified version of the Daikon invariant detector version 5.8.10, configured for detecting invariants
in the context of RESTful APIs. The original software can be found here: https://plse.cs.washington.edu/daikon/

This version of Daikon has been modified by suppressing specific invariants and derived variables (that could result in the generation of
false positives) and adding new domain-specific invariants and derived variables.

# Invariants
## Suppressed invariants
Invariants can be suppressed by commenting their corresponding line in the setup_proto_invs function of the Daikon.java
file. The following invariants have been suppressed:

- IntNonEqual
- FloatNonEqual
- StringNonEqual
- StringLessThan
- StringGreaterThan
- StringLessEqual
- StringGreaterEqual
- SeqSeqStringEqual
- SeqSeqStringLessThan
- SeqSeqStringGreaterThan
- SeqSeqStringLessEqual
- SeqSeqStringGreaterEqual
- PairwiseStringLessThan
- PairwiseStringGreaterThan
- PairwiseStringLessEqual
- PairwiseStringGreaterEqual
- NumericInt$Divides
- NumericFloat$Divides
- NonZeroFloat

# New invariants
## Unary invariants
- Url (True if a string is a URL)
- FixedLengthString (True if a string always has the same length)
- IsNumeric (True if a string is numeric)
- IsEmail (True if a string is an email)
- IsDate, formats:
	- YYYY/MM/DD ("/" can be replaced by "-" or "."):	
	- DD/MM/YYYY
	- MM/DD/YYYY
	- HH:MM 24-hour format, optional leading 0
	- HH:MM:SS 24-hour format with optional leading 0
	- HH:MM 12-hour format, optional leading 0, mandatory meridiems (AM/PM)
	- Format: YYYY-MM-DDTHH:MM:SS.mmZ (Miliseconds are optional)

## Unary invariants for sequences
- SequenceStringElementsAreUrl (True if all the elements of an array are of type URL)
- SequenceFixedLengthString (True if all the elements of an array have the same length)
- SequenceStringElementsAreNumeric (True if all the elements of an array are Numeric)
- SequenceStringElementsAreEmail (True if all the elements of an array are Email)
- Elements of sequence are dates. Formats:
	- YYYY/MM/DD ("/" can be replaced by "-" or "."):	
	- DD/MM/YYYY
	- MM/DD/YYYY
	- HH:MM 24-hour format, optional leading 0
	- HH:MM:SS 24-hour format with optional leading 0
	- HH:MM 12-hour format, optional leading 0, mandatory meridiems (AM/PM)
	- Format: YYYY-MM-DDTHH:MM:SS.mmZ (Miliseconds are optional)

# Derived variables
## Suppressed derived variables
- orig() derived variables
- Removed shift (Now it is always zero) from SequenceLength

# Suppressed variables
- FixedLengthString is suppressed by YYYY/MM/DD, DD/MM/YYYY and MM/DD/YYYY

# Modifications in computeConfidence
Modified the computeConfidence function of Invariant types that simply returned "Invariant.CONFIDENCE_JUSTIFIED"
- unary:
	- scalar:
		- RangeFloat
		- RangeInt
	- sequence:
		- EltRangeFloat
		- EltRangeInt
- MemberString:
	- sequenceScalar:
		- Member
		- MemberFloat
	- sequenceString:
		- MemberString
	- twoSequence:
		- Reverse
		- ReverseFloat
		- SubSequence
		- SubSequenceFloat
		- SubSet
		- SubSetFloat
		- SuperSequence
		- SuperSequenceFloat
		- SuperSet
		- SuperSetFloat