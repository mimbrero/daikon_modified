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

## New invariants
### Unary invariants
- Url (True if a string is a URL)
- FixedLengthString (True if a string always has the same length)

# Derived variables
## Suppressed derived variables
- 