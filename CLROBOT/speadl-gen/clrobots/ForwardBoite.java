package clrobots;

@SuppressWarnings("all")
public abstract class ForwardBoite<IBoiteInfo> {
  public interface Requires<IBoiteInfo> {
  }
  
  public interface Component<IBoiteInfo> extends ForwardBoite.Provides<IBoiteInfo> {
  }
  
  public interface Provides<IBoiteInfo> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IBoiteInfo i();
  }
  
  public interface Parts<IBoiteInfo> {
  }
  
  public static class ComponentImpl<IBoiteInfo> implements ForwardBoite.Component<IBoiteInfo>, ForwardBoite.Parts<IBoiteInfo> {
    private final ForwardBoite.Requires<IBoiteInfo> bridge;
    
    private final ForwardBoite<IBoiteInfo> implementation;
    
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
      	throw new RuntimeException("make_i() in clrobots.ForwardBoite<IBoiteInfo> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_i();
    }
    
    public ComponentImpl(final ForwardBoite<IBoiteInfo> implem, final ForwardBoite.Requires<IBoiteInfo> b, final boolean doInits) {
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
    
    private IBoiteInfo i;
    
    public IBoiteInfo i() {
      return this.i;
    }
  }
  
  public static class AgentForward<IBoiteInfo> {
    public interface Requires<IBoiteInfo> {
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public IBoiteInfo a();
    }
    
    public interface Component<IBoiteInfo> extends ForwardBoite.AgentForward.Provides<IBoiteInfo> {
    }
    
    public interface Provides<IBoiteInfo> {
    }
    
    public interface Parts<IBoiteInfo> {
    }
    
    public static class ComponentImpl<IBoiteInfo> implements ForwardBoite.AgentForward.Component<IBoiteInfo>, ForwardBoite.AgentForward.Parts<IBoiteInfo> {
      private final ForwardBoite.AgentForward.Requires<IBoiteInfo> bridge;
      
      private final ForwardBoite.AgentForward<IBoiteInfo> implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final ForwardBoite.AgentForward<IBoiteInfo> implem, final ForwardBoite.AgentForward.Requires<IBoiteInfo> b, final boolean doInits) {
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
    
    private ForwardBoite.AgentForward.ComponentImpl<IBoiteInfo> selfComponent;
    
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
    protected ForwardBoite.AgentForward.Provides<IBoiteInfo> provides() {
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
    protected ForwardBoite.AgentForward.Requires<IBoiteInfo> requires() {
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
    protected ForwardBoite.AgentForward.Parts<IBoiteInfo> parts() {
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
    public synchronized ForwardBoite.AgentForward.Component<IBoiteInfo> _newComponent(final ForwardBoite.AgentForward.Requires<IBoiteInfo> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of AgentForward has already been used to create a component, use another one.");
      }
      this.init = true;
      ForwardBoite.AgentForward.ComponentImpl<IBoiteInfo>  _comp = new ForwardBoite.AgentForward.ComponentImpl<IBoiteInfo>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private ForwardBoite.ComponentImpl<IBoiteInfo> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected ForwardBoite.Provides<IBoiteInfo> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected ForwardBoite.Requires<IBoiteInfo> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected ForwardBoite.Parts<IBoiteInfo> eco_parts() {
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
  
  private ForwardBoite.ComponentImpl<IBoiteInfo> selfComponent;
  
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
  protected ForwardBoite.Provides<IBoiteInfo> provides() {
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
  protected abstract IBoiteInfo make_i();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected ForwardBoite.Requires<IBoiteInfo> requires() {
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
  protected ForwardBoite.Parts<IBoiteInfo> parts() {
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
  public synchronized ForwardBoite.Component<IBoiteInfo> _newComponent(final ForwardBoite.Requires<IBoiteInfo> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ForwardBoite has already been used to create a component, use another one.");
    }
    this.init = true;
    ForwardBoite.ComponentImpl<IBoiteInfo>  _comp = new ForwardBoite.ComponentImpl<IBoiteInfo>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected ForwardBoite.AgentForward<IBoiteInfo> make_AgentForward() {
    return new ForwardBoite.AgentForward<IBoiteInfo>();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public ForwardBoite.AgentForward<IBoiteInfo> _createImplementationOfAgentForward() {
    ForwardBoite.AgentForward<IBoiteInfo> implem = make_AgentForward();
    if (implem == null) {
    	throw new RuntimeException("make_AgentForward() in clrobots.ForwardBoite should not return null.");
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
  public ForwardBoite.Component<IBoiteInfo> newComponent() {
    return this._newComponent(new ForwardBoite.Requires<IBoiteInfo>() {}, true);
  }
}
