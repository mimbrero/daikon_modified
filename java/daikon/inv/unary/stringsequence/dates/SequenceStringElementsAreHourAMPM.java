package daikon.inv.unary.stringsequence.dates;

import daikon.PptSlice;
import daikon.inv.DiscardInfo;
import daikon.inv.Invariant;
import daikon.inv.InvariantStatus;
import daikon.inv.OutputFormat;
import daikon.inv.unary.stringsequence.SingleStringSequence;
import org.checkerframework.checker.interning.qual.Interned;
import org.checkerframework.checker.lock.qual.GuardSatisfied;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;
import typequals.prototype.qual.Prototype;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents string sequences that contain a common subset. Prints as {@code {s1, s2, s3, ...}
 * subset of x[]}.
 */
public class SequenceStringElementsAreHourAMPM extends SingleStringSequence {
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20220423L;

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  public static boolean dkconfig_enabled = Invariant.invariantEnabledDefault;

  protected SequenceStringElementsAreHourAMPM(PptSlice ppt) {
    super(ppt);

  }

  protected @Prototype
  SequenceStringElementsAreHourAMPM() {
    super();
  }

  private static @Prototype SequenceStringElementsAreHourAMPM proto = new @Prototype SequenceStringElementsAreHourAMPM();

  /** Returns the prototype invariant for CommonStringSequence. */
  public static @Prototype SequenceStringElementsAreHourAMPM get_proto() {
    return proto;
  }

  /** returns whether or not this invariant is enabled */
  @Override
  public boolean enabled() {
    return dkconfig_enabled;
  }

  /** instantiate an invariant on the specified slice */
  @Override
  protected SequenceStringElementsAreHourAMPM instantiate_dyn(
          @Prototype SequenceStringElementsAreHourAMPM this, PptSlice slice) {
    return new SequenceStringElementsAreHourAMPM(slice);
  }

  // Don't write clone, because this.intersect is read-only
  // protected Object clone();

  @Override
  public String repr(@GuardSatisfied SequenceStringElementsAreHourAMPM this) {
    return "SequenceStringElementsAreHourAMPM " + varNames();
  }

  @SideEffectFree
  @Override
  public String format_using(@GuardSatisfied SequenceStringElementsAreHourAMPM this, OutputFormat format) {
    return "All the elements of " + var().name() + " are Hours: HH:MM 12-hour format, optional leading 0, mandatory meridiems (AM/PM)";
  }


  @Override
  public InvariantStatus check_modified(@Interned String @Interned [] a, int count) {

    Pattern pattern = Pattern.compile("^((1[0-2]|0?[1-9]):([0-5][0-9]) ?([AaPp][Mm]))$");
    
    for(int i=0; i<a.length; i++) {
      Matcher matcher = pattern.matcher(a[i]);
      if(!matcher.matches()) {
        return InvariantStatus.FALSIFIED;
      }
    }

    return InvariantStatus.NO_CHANGE;

  }

  @Override
  public InvariantStatus add_modified(@Interned String @Interned [] a, int count) {
    return check_modified(a, count);
  }

  @Override
  protected double computeConfidence() {
    return 1 - Math.pow(.1, ppt.num_samples());
  }

  @Pure
  public @Nullable DiscardInfo isObviousImplied() {
    return null;
  }

  @Pure
  @Override
  public boolean isSameFormula(Invariant other) {
    assert other instanceof SequenceStringElementsAreHourAMPM;
    return true;
  }
}
