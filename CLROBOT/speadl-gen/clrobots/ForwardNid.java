package clrobots;

@SuppressWarnings("all")
public abstract class ForwardNid<INidInfo> {
  public interface Requires<INidInfo> {
  }
  
  public interface Component<INidInfo> extends ForwardNid.Provides<INidInfo> {
  }
  
  public interface Provides<INidInfo> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public INidInfo i();
  }
  
  public interface Parts<INidInfo> {
  }
  
  public static class ComponentImpl<INidInfo> implements ForwardNid.Component<INidInfo>, ForwardNid.Parts<INidInfo> {
    private final ForwardNid.Requires<INidInfo> bridge;
    
    private final ForwardNid<INidInfo> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_i() {
      assert this.i == null: "This is a bug.";
      this.i = this.implementation.make_i();
      if (this.i == null) {
      	throw new RuntimeException("make_i() in clrobots.ForwardNid<INidInfo> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_i();
    }
    
    public ComponentImpl(final ForwardNid<INidInfo> implem, final ForwardNid.Requires<INidInfo> b, final boolean doInits) {
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
    
    private INidInfo i;
    
    public INidInfo i() {
      return this.i;
    }
  }
  
  public static class AgentForward<INidInfo> {
    public interface Requires<INidInfo> {
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public INidInfo a();
    }
    
    public interface Component<INidInfo> extends ForwardNid.AgentForward.Provides<INidInfo> {
    }
    
    public interface Provides<INidInfo> {
    }
    
    public interface Parts<INidInfo> {
    }
    
    public static class ComponentImpl<INidInfo> implements ForwardNid.AgentForward.Component<INidInfo>, ForwardNid.AgentForward.Parts<INidInfo> {
      private final ForwardNid.AgentForward.Requires<INidInfo> bridge;
      
      private final ForwardNid.AgentForward<INidInfo> implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final ForwardNid.AgentForward<INidInfo> implem, final ForwardNid.AgentForward.Requires<INidInfo> b, final boolean doInits) {
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
    
    private ForwardNid.AgentForward.ComponentImpl<INidInfo> selfComponent;
    
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
    protected ForwardNid.AgentForward.Provides<INidInfo> provides() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected ForwardNid.AgentForward.Requires<INidInfo> requires() {
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
    protected ForwardNid.AgentForward.Parts<INidInfo> parts() {
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
    public synchronized ForwardNid.AgentForward.Component<INidInfo> _newComponent(final ForwardNid.AgentForward.Requires<INidInfo> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of AgentForward has already been used to create a component, use another one.");
      }
      this.init = true;
      ForwardNid.AgentForward.ComponentImpl<INidInfo>  _comp = new ForwardNid.AgentForward.ComponentImpl<INidInfo>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private ForwardNid.ComponentImpl<INidInfo> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected ForwardNid.Provides<INidInfo> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected ForwardNid.Requires<INidInfo> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected ForwardNid.Parts<INidInfo> eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
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
  
  private ForwardNid.ComponentImpl<INidInfo> selfComponent;
  
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
  protected ForwardNid.Provides<INidInfo> provides() {
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
  protected abstract INidInfo make_i();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected ForwardNid.Requires<INidInfo> requires() {
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
  protected ForwardNid.Parts<INidInfo> parts() {
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
  public synchronized ForwardNid.Component<INidInfo> _newComponent(final ForwardNid.Requires<INidInfo> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ForwardNid has already been used to create a component, use another one.");
    }
    this.init = true;
    ForwardNid.ComponentImpl<INidInfo>  _comp = new ForwardNid.ComponentImpl<INidInfo>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected ForwardNid.AgentForward<INidInfo> make_AgentForward() {
    return new ForwardNid.AgentForward<INidInfo>();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public ForwardNid.AgentForward<INidInfo> _createImplementationOfAgentForward() {
    ForwardNid.AgentForward<INidInfo> implem = make_AgentForward();
    if (implem == null) {
    	throw new RuntimeException("make_AgentForward() in clrobots.ForwardNid should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public ForwardNid.Component<INidInfo> newComponent() {
    return this._newComponent(new ForwardNid.Requires<INidInfo>() {}, true);
  }
}
