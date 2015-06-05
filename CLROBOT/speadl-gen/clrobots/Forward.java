package clrobots;

@SuppressWarnings("all")
public abstract class Forward<I, J, K> {
  public interface Requires<I, J, K> {
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
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public K k();
  }
  
  public interface Component<I, J, K> extends Forward.Provides<I, J, K> {
  }
  
  public interface Provides<I, J, K> {
  }
  
  public interface Parts<I, J, K> {
  }
  
  public static class ComponentImpl<I, J, K> implements Forward.Component<I, J, K>, Forward.Parts<I, J, K> {
    private final Forward.Requires<I, J, K> bridge;
    
    private final Forward<I, J, K> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final Forward<I, J, K> implem, final Forward.Requires<I, J, K> b, final boolean doInits) {
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
  
  public static abstract class Agent<I, J, K> {
    public interface Requires<I, J, K> {
    }
    
    public interface Component<I, J, K> extends Forward.Agent.Provides<I, J, K> {
    }
    
    public interface Provides<I, J, K> {
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
      
      /**
       * This can be called to access the provided port.
       * 
       */
      public K c();
    }
    
    public interface Parts<I, J, K> {
    }
    
    public static class ComponentImpl<I, J, K> implements Forward.Agent.Component<I, J, K>, Forward.Agent.Parts<I, J, K> {
      private final Forward.Agent.Requires<I, J, K> bridge;
      
      private final Forward.Agent<I, J, K> implementation;
      
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
        	throw new RuntimeException("make_a() in clrobots.Forward$Agent<I, J, K> should not return null.");
        }
      }
      
      private void init_b() {
        assert this.b == null: "This is a bug.";
        this.b = this.implementation.make_b();
        if (this.b == null) {
        	throw new RuntimeException("make_b() in clrobots.Forward$Agent<I, J, K> should not return null.");
        }
      }
      
      private void init_c() {
        assert this.c == null: "This is a bug.";
        this.c = this.implementation.make_c();
        if (this.c == null) {
        	throw new RuntimeException("make_c() in clrobots.Forward$Agent<I, J, K> should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_a();
        init_b();
        init_c();
      }
      
      public ComponentImpl(final Forward.Agent<I, J, K> implem, final Forward.Agent.Requires<I, J, K> b, final boolean doInits) {
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
      
      private K c;
      
      public K c() {
        return this.c;
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
    
    private Forward.Agent.ComponentImpl<I, J, K> selfComponent;
    
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
    protected Forward.Agent.Provides<I, J, K> provides() {
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
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract K make_c();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected Forward.Agent.Requires<I, J, K> requires() {
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
    protected Forward.Agent.Parts<I, J, K> parts() {
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
    public synchronized Forward.Agent.Component<I, J, K> _newComponent(final Forward.Agent.Requires<I, J, K> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Agent has already been used to create a component, use another one.");
      }
      this.init = true;
      Forward.Agent.ComponentImpl<I, J, K>  _comp = new Forward.Agent.ComponentImpl<I, J, K>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private Forward.ComponentImpl<I, J, K> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected Forward.Provides<I, J, K> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected Forward.Requires<I, J, K> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected Forward.Parts<I, J, K> eco_parts() {
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
  
  private Forward.ComponentImpl<I, J, K> selfComponent;
  
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
  protected Forward.Provides<I, J, K> provides() {
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
  protected Forward.Requires<I, J, K> requires() {
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
  protected Forward.Parts<I, J, K> parts() {
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
  public synchronized Forward.Component<I, J, K> _newComponent(final Forward.Requires<I, J, K> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Forward has already been used to create a component, use another one.");
    }
    this.init = true;
    Forward.ComponentImpl<I, J, K>  _comp = new Forward.ComponentImpl<I, J, K>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract Forward.Agent<I, J, K> make_Agent();
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public Forward.Agent<I, J, K> _createImplementationOfAgent() {
    Forward.Agent<I, J, K> implem = make_Agent();
    if (implem == null) {
    	throw new RuntimeException("make_Agent() in clrobots.Forward should not return null.");
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
  protected Forward.Agent.Component<I, J, K> newAgent() {
    Forward.Agent<I, J, K> _implem = _createImplementationOfAgent();
    return _implem._newComponent(new Forward.Agent.Requires<I, J, K>() {},true);
  }
}
