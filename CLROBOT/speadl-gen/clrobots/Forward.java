package clrobots;

@SuppressWarnings("all")
public abstract class Forward<I, J, K, Push, Pull> {
  public interface Requires<I, J, K, Push, Pull> {
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
  
  public interface Component<I, J, K, Push, Pull> extends Forward.Provides<I, J, K, Push, Pull> {
  }
  
  public interface Provides<I, J, K, Push, Pull> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Push push();
  }
  
  public interface Parts<I, J, K, Push, Pull> {
  }
  
  public static class ComponentImpl<I, J, K, Push, Pull> implements Forward.Component<I, J, K, Push, Pull>, Forward.Parts<I, J, K, Push, Pull> {
    private final Forward.Requires<I, J, K, Push, Pull> bridge;
    
    private final Forward<I, J, K, Push, Pull> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_push() {
      assert this.push == null: "This is a bug.";
      this.push = this.implementation.make_push();
      if (this.push == null) {
      	throw new RuntimeException("make_push() in clrobots.Forward<I, J, K, Push, Pull> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_push();
    }
    
    public ComponentImpl(final Forward<I, J, K, Push, Pull> implem, final Forward.Requires<I, J, K, Push, Pull> b, final boolean doInits) {
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
    
    private Push push;
    
    public Push push() {
      return this.push;
    }
  }
  
  public static abstract class Agent<I, J, K, Push, Pull> {
    public interface Requires<I, J, K, Push, Pull> {
    }
    
    public interface Component<I, J, K, Push, Pull> extends Forward.Agent.Provides<I, J, K, Push, Pull> {
    }
    
    public interface Provides<I, J, K, Push, Pull> {
      /**
       * This can be called to access the provided port.
       * 
       */
      public Push push();
      
      /**
       * This can be called to access the provided port.
       * 
       */
      public Pull pull();
      
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
    
    public interface Parts<I, J, K, Push, Pull> {
    }
    
    public static class ComponentImpl<I, J, K, Push, Pull> implements Forward.Agent.Component<I, J, K, Push, Pull>, Forward.Agent.Parts<I, J, K, Push, Pull> {
      private final Forward.Agent.Requires<I, J, K, Push, Pull> bridge;
      
      private final Forward.Agent<I, J, K, Push, Pull> implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_push() {
        assert this.push == null: "This is a bug.";
        this.push = this.implementation.make_push();
        if (this.push == null) {
        	throw new RuntimeException("make_push() in clrobots.Forward$Agent<I, J, K, Push, Pull> should not return null.");
        }
      }
      
      private void init_pull() {
        assert this.pull == null: "This is a bug.";
        this.pull = this.implementation.make_pull();
        if (this.pull == null) {
        	throw new RuntimeException("make_pull() in clrobots.Forward$Agent<I, J, K, Push, Pull> should not return null.");
        }
      }
      
      private void init_a() {
        assert this.a == null: "This is a bug.";
        this.a = this.implementation.make_a();
        if (this.a == null) {
        	throw new RuntimeException("make_a() in clrobots.Forward$Agent<I, J, K, Push, Pull> should not return null.");
        }
      }
      
      private void init_b() {
        assert this.b == null: "This is a bug.";
        this.b = this.implementation.make_b();
        if (this.b == null) {
        	throw new RuntimeException("make_b() in clrobots.Forward$Agent<I, J, K, Push, Pull> should not return null.");
        }
      }
      
      private void init_c() {
        assert this.c == null: "This is a bug.";
        this.c = this.implementation.make_c();
        if (this.c == null) {
        	throw new RuntimeException("make_c() in clrobots.Forward$Agent<I, J, K, Push, Pull> should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_push();
        init_pull();
        init_a();
        init_b();
        init_c();
      }
      
      public ComponentImpl(final Forward.Agent<I, J, K, Push, Pull> implem, final Forward.Agent.Requires<I, J, K, Push, Pull> b, final boolean doInits) {
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
      
      private Push push;
      
      public Push push() {
        return this.push;
      }
      
      private Pull pull;
      
      public Pull pull() {
        return this.pull;
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
    
    private Forward.Agent.ComponentImpl<I, J, K, Push, Pull> selfComponent;
    
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
    protected Forward.Agent.Provides<I, J, K, Push, Pull> provides() {
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
    protected abstract Push make_push();
    
    /**
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract Pull make_pull();
    
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
    protected Forward.Agent.Requires<I, J, K, Push, Pull> requires() {
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
    protected Forward.Agent.Parts<I, J, K, Push, Pull> parts() {
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
    public synchronized Forward.Agent.Component<I, J, K, Push, Pull> _newComponent(final Forward.Agent.Requires<I, J, K, Push, Pull> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Agent has already been used to create a component, use another one.");
      }
      this.init = true;
      Forward.Agent.ComponentImpl<I, J, K, Push, Pull>  _comp = new Forward.Agent.ComponentImpl<I, J, K, Push, Pull>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private Forward.ComponentImpl<I, J, K, Push, Pull> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected Forward.Provides<I, J, K, Push, Pull> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected Forward.Requires<I, J, K, Push, Pull> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected Forward.Parts<I, J, K, Push, Pull> eco_parts() {
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
  
  private Forward.ComponentImpl<I, J, K, Push, Pull> selfComponent;
  
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
  protected Forward.Provides<I, J, K, Push, Pull> provides() {
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
  protected abstract Push make_push();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Forward.Requires<I, J, K, Push, Pull> requires() {
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
  protected Forward.Parts<I, J, K, Push, Pull> parts() {
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
  public synchronized Forward.Component<I, J, K, Push, Pull> _newComponent(final Forward.Requires<I, J, K, Push, Pull> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Forward has already been used to create a component, use another one.");
    }
    this.init = true;
    Forward.ComponentImpl<I, J, K, Push, Pull>  _comp = new Forward.ComponentImpl<I, J, K, Push, Pull>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract Forward.Agent<I, J, K, Push, Pull> make_Agent(final String id);
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public Forward.Agent<I, J, K, Push, Pull> _createImplementationOfAgent(final String id) {
    Forward.Agent<I, J, K, Push, Pull> implem = make_Agent(id);
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
  protected Forward.Agent.Component<I, J, K, Push, Pull> newAgent(final String id) {
    Forward.Agent<I, J, K, Push, Pull> _implem = _createImplementationOfAgent(id);
    return _implem._newComponent(new Forward.Agent.Requires<I, J, K, Push, Pull>() {},true);
  }
}
