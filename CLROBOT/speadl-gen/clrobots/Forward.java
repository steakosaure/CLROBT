package clrobots;

@SuppressWarnings("all")
public abstract class Forward<I, J> {
  public interface Requires<I, J> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public I i();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public J j();
  }
  
  public interface Component<I, J> extends Forward.Provides<I, J> {
  }
  
  public interface Provides<I, J> {
  }
  
  public interface Parts<I, J> {
  }
  
  public static class ComponentImpl<I, J> implements Forward.Component<I, J>, Forward.Parts<I, J> {
    private final Forward.Requires<I, J> bridge;
    
    private final Forward<I, J> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final Forward<I, J> implem, final Forward.Requires<I, J> b, final boolean doInits) {
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
  
  public static abstract class AgentForward<I, J> {
    public interface Requires<I, J> {
    }
    
    public interface Component<I, J> extends Forward.AgentForward.Provides<I, J> {
    }
    
    public interface Provides<I, J> {
      /**
       * This can be called to access the provided port.
       * 
       */
      public I a();
      
      /**
       * This can be called to access the provided port.
       * 
       */
      public J b();
    }
    
    public interface Parts<I, J> {
    }
    
    public static class ComponentImpl<I, J> implements Forward.AgentForward.Component<I, J>, Forward.AgentForward.Parts<I, J> {
      private final Forward.AgentForward.Requires<I, J> bridge;
      
      private final Forward.AgentForward<I, J> implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_a() {
        assert this.a == null: "This is a bug.";
        this.a = this.implementation.make_a();
        if (this.a == null) {
        	throw new RuntimeException("make_a() in clrobots.Forward$AgentForward<I, J> should not return null.");
        }
      }
      
      private void init_b() {
        assert this.b == null: "This is a bug.";
        this.b = this.implementation.make_b();
        if (this.b == null) {
        	throw new RuntimeException("make_b() in clrobots.Forward$AgentForward<I, J> should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_a();
        init_b();
      }
      
      public ComponentImpl(final Forward.AgentForward<I, J> implem, final Forward.AgentForward.Requires<I, J> b, final boolean doInits) {
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
      
      private I a;
      
      public I a() {
        return this.a;
      }
      
      private J b;
      
      public J b() {
        return this.b;
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
    
    private Forward.AgentForward.ComponentImpl<I, J> selfComponent;
    
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
    protected Forward.AgentForward.Provides<I, J> provides() {
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
    protected abstract I make_a();
    
    /**
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract J make_b();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected Forward.AgentForward.Requires<I, J> requires() {
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
    protected Forward.AgentForward.Parts<I, J> parts() {
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
    public synchronized Forward.AgentForward.Component<I, J> _newComponent(final Forward.AgentForward.Requires<I, J> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of AgentForward has already been used to create a component, use another one.");
      }
      this.init = true;
      Forward.AgentForward.ComponentImpl<I, J>  _comp = new Forward.AgentForward.ComponentImpl<I, J>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private Forward.ComponentImpl<I, J> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected Forward.Provides<I, J> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected Forward.Requires<I, J> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected Forward.Parts<I, J> eco_parts() {
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
  
  private Forward.ComponentImpl<I, J> selfComponent;
  
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
  protected Forward.Provides<I, J> provides() {
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
  protected Forward.Requires<I, J> requires() {
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
  protected Forward.Parts<I, J> parts() {
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
  public synchronized Forward.Component<I, J> _newComponent(final Forward.Requires<I, J> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Forward has already been used to create a component, use another one.");
    }
    this.init = true;
    Forward.ComponentImpl<I, J>  _comp = new Forward.ComponentImpl<I, J>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract Forward.AgentForward<I, J> make_AgentForward();
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public Forward.AgentForward<I, J> _createImplementationOfAgentForward() {
    Forward.AgentForward<I, J> implem = make_AgentForward();
    if (implem == null) {
    	throw new RuntimeException("make_AgentForward() in clrobots.Forward should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected Forward.AgentForward.Component<I, J> newAgentForward() {
    Forward.AgentForward<I, J> _implem = _createImplementationOfAgentForward();
    return _implem._newComponent(new Forward.AgentForward.Requires<I, J>() {},true);
  }
}
