package clrobots;

@SuppressWarnings("all")
public abstract class Knowledge<SelfKnowledge> {
  public interface Requires<SelfKnowledge> {
  }
  
  public interface Component<SelfKnowledge> extends Knowledge.Provides<SelfKnowledge> {
  }
  
  public interface Provides<SelfKnowledge> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public SelfKnowledge selfKnowledge();
  }
  
  public interface Parts<SelfKnowledge> {
  }
  
  public static class ComponentImpl<SelfKnowledge> implements Knowledge.Component<SelfKnowledge>, Knowledge.Parts<SelfKnowledge> {
    private final Knowledge.Requires<SelfKnowledge> bridge;
    
    private final Knowledge<SelfKnowledge> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_selfKnowledge() {
      assert this.selfKnowledge == null: "This is a bug.";
      this.selfKnowledge = this.implementation.make_selfKnowledge();
      if (this.selfKnowledge == null) {
      	throw new RuntimeException("make_selfKnowledge() in clrobots.Knowledge<SelfKnowledge> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_selfKnowledge();
    }
    
    public ComponentImpl(final Knowledge<SelfKnowledge> implem, final Knowledge.Requires<SelfKnowledge> b, final boolean doInits) {
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
    
    private SelfKnowledge selfKnowledge;
    
    public SelfKnowledge selfKnowledge() {
      return this.selfKnowledge;
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
  
  private Knowledge.ComponentImpl<SelfKnowledge> selfComponent;
  
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
  protected Knowledge.Provides<SelfKnowledge> provides() {
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
  protected abstract SelfKnowledge make_selfKnowledge();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Knowledge.Requires<SelfKnowledge> requires() {
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
  protected Knowledge.Parts<SelfKnowledge> parts() {
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
  public synchronized Knowledge.Component<SelfKnowledge> _newComponent(final Knowledge.Requires<SelfKnowledge> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Knowledge has already been used to create a component, use another one.");
    }
    this.init = true;
    Knowledge.ComponentImpl<SelfKnowledge>  _comp = new Knowledge.ComponentImpl<SelfKnowledge>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public Knowledge.Component<SelfKnowledge> newComponent() {
    return this._newComponent(new Knowledge.Requires<SelfKnowledge>() {}, true);
  }
}
