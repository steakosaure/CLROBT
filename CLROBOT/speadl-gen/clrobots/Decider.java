package clrobots;

import clrobots.interfaces.Do;

@SuppressWarnings("all")
public abstract class Decider<Actionable, SelfKnowledge> {
  public interface Requires<Actionable, SelfKnowledge> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Actionable action();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public SelfKnowledge knowledge();
  }
  
  public interface Component<Actionable, SelfKnowledge> extends Decider.Provides<Actionable, SelfKnowledge> {
  }
  
  public interface Provides<Actionable, SelfKnowledge> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Do decision();
  }
  
  public interface Parts<Actionable, SelfKnowledge> {
  }
  
  public static class ComponentImpl<Actionable, SelfKnowledge> implements Decider.Component<Actionable, SelfKnowledge>, Decider.Parts<Actionable, SelfKnowledge> {
    private final Decider.Requires<Actionable, SelfKnowledge> bridge;
    
    private final Decider<Actionable, SelfKnowledge> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_decision() {
      assert this.decision == null: "This is a bug.";
      this.decision = this.implementation.make_decision();
      if (this.decision == null) {
      	throw new RuntimeException("make_decision() in clrobots.Decider<Actionable, SelfKnowledge> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_decision();
    }
    
    public ComponentImpl(final Decider<Actionable, SelfKnowledge> implem, final Decider.Requires<Actionable, SelfKnowledge> b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    private Do decision;
    
    public Do decision() {
      return this.decision;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private Decider.ComponentImpl<Actionable, SelfKnowledge> selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected Decider.Provides<Actionable, SelfKnowledge> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract Do make_decision();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Decider.Requires<Actionable, SelfKnowledge> requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected Decider.Parts<Actionable, SelfKnowledge> parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized Decider.Component<Actionable, SelfKnowledge> _newComponent(final Decider.Requires<Actionable, SelfKnowledge> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Decider has already been used to create a component, use another one.");
    }
    this.init = true;
    Decider.ComponentImpl<Actionable, SelfKnowledge>  _comp = new Decider.ComponentImpl<Actionable, SelfKnowledge>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
